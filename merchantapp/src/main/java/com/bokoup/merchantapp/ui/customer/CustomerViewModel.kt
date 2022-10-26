package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.TokenAccountListQuery
import com.bokoup.merchantapp.data.DataRepo
import com.bokoup.merchantapp.model.DelegateMemo
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator,
    private val dataRepo: DataRepo
) : ViewModel() {
    val qrCodeConsumer = ResourceFlowConsumer<Bitmap>(viewModelScope)
    val delegateTokenSubscription = dataRepo.delegateTokenSubscription
    fun getQrCode(orderId: String, tokenAccounts: List<TokenAccountListQuery.TokenAccount>, checkedState: Map<String, Boolean>, timestamp: Int) = viewModelScope.launch(Dispatchers.IO) {
        val checked = checkedState.entries.first { it.value }
        val tokenAccount = tokenAccounts.first{ it.id == checked.key }
        val mintString = tokenAccount.mintObject?.id
        val message = "Approve to delegate one ${tokenAccount.mintObject?.promoObject?.metadataObject?.name} token"
//        val memo = "{\"orderId\": \"$orderId\", \"timestamp\": $timestamp}"
        val memo = DelegateMemo(orderId, timestamp)
        val memoJson = Gson().toJson(memo)
        qrCodeConsumer.collectFlow(
            resourceFlowOf {
                val url = URL("http://99.91.8.130:8080/promo/delegate/$mintString/${URLEncoder.encode(message, "utf-8")}/${URLEncoder.encode(memoJson, "utf-8")}")
                val content = "solana:$url"
                qrCodeGenerator.generateQR(content)
            }
        )
    }
    fun cancel(activity: Activity) {
        val data = Intent()
//        data.putExtra(Intents.EXTRA_DECLINE_REASON, "Cancelled!")
//        activity.setResult(RESULT_CANCELED, data)
        activity.finish()
    }

    fun approve(activity: Activity) {
        activity.finish();
    }

//    fun createSignatureSubscription(signature: TransactionSignature, commitment: Commitment = Commitment.CONFIRMED) {
//        _signatureSubscription = solanaRepo.solanaSubscription.signatureSubscribe(signature, Commitment.CONFIRMED)
//        solanaRepo.solanaSubscription.connect()
//    }

//    solanaSubscription.signatureSubscribe(signature, Commitment.CONFIRMED).collect {
//        try {
//            if (it is TransactionSignatureStatus.Confirmed) {
//                solanaSubscription.disconnect()
//                emit(Resource.Success(signature))
//            }
//        } catch (error: Throwable) {
//            solanaSubscription.disconnect()
//            emit(Resource.Error(error))
//
//        }
//    }


//    com.clover.sdk.app.intent.action.APP_NOTIFICATION


}