package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.domain.CustomerRepo
import com.bokoup.merchantapp.model.DelegateMemo
import com.bokoup.merchantapp.model.TokenAccountWithMetadata
import com.clover.sdk.v3.order.Discount
import com.clover.sdk.v3.order.Order
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator, private val repo: CustomerRepo
) : ViewModel() {
    val qrCodeConsumer = ResourceFlowConsumer<Bitmap>(viewModelScope)
    val delegateTokenSubscription = repo.delegateTokenSubscription
    val orderConsumer = ResourceFlowConsumer<Order>(viewModelScope)
    fun getQrCode(
        orderId: String,
        tokenAccount: TokenAccountWithMetadata,
        delegateString: String,
        timestamp: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        val mintString = tokenAccount.tokenAccount.mintObject?.id
        val message =
            "Approve to delegate one ${tokenAccount.tokenAccount.mintObject?.promoObject?.metadataObject?.name} token"
        val memo = DelegateMemo(orderId, timestamp)
        val memoJson = Gson().toJson(memo)

        qrCodeConsumer.collectFlow(resourceFlowOf {
            val url = URL(
                "http://99.91.8.130:8080/promo/delegate/$mintString/$delegateString/${
                    withContext(Dispatchers.IO) {
                        URLEncoder.encode(
                            message, "utf-8"
                        )
                    }
                }/${
                    withContext(Dispatchers.IO) {
                        URLEncoder.encode(memoJson, "utf-8")
                    }
                }"
            )
            val content = "solana:$url"
            Log.d("content", content)
            qrCodeGenerator.generateQR(content)
        })
    }

    fun cancel(activity: Activity) {
        activity.setResult(RESULT_CANCELED)
        activity.finish()
    }

    fun approve(activity: Activity) {
        activity.setResult(RESULT_OK)
        activity.finish();
    }

    fun getOrder(orderId: String) {
        orderConsumer.collectFlow(
            repo.getOrder(orderId)
        )
    }

    suspend fun addDiscount(orderId: String, discount: Discount) {

        withContext(Dispatchers.IO) {
            repo.connectOrders()
        }

        orderConsumer.collectFlow(
            repo.addDiscount(orderId, discount)
        )
    }

    suspend fun disconnectOrders() {
        withContext(Dispatchers.IO) {
            repo.disconnectOrders()
        }
    }

}