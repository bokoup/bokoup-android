package com.bokoup.merchantapp.ui.promo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.domain.DataRepo
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.CreatePromoResult
import com.bokoup.merchantapp.model.PromoWithMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class PromoViewModel @Inject constructor(
    private val dataRepo: DataRepo,
    private val qrCodeGenerator: QRCodeGenerator,
) : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    val promoSubscription = dataRepo.promoSubscriptionFlow

    val promosConsumer = ResourceFlowConsumer<List<PromoWithMetadata>>(viewModelScope)
    val appIdConsumer = ResourceFlowConsumer<AppId>(viewModelScope)
    val createPromoConsumer = ResourceFlowConsumer<CreatePromoResult>(viewModelScope)

    val errorConsumer = merge(
        promosConsumer.error,
        appIdConsumer.error,
        createPromoConsumer.error,
    )

    val isLoadingConsumer = merge(
        promosConsumer.isLoading,
    )

    val qrCodeConsumer = ResourceFlowConsumer<Bitmap>(viewModelScope)

    fun getQrCode(mintString: String, message: String) = viewModelScope.launch(Dispatchers.IO) {
        qrCodeConsumer.collectFlow(
            resourceFlowOf {
                val url = URL("http://99.91.8.130:8080/promo/mint/$mintString/${URLEncoder.encode(message, "utf-8")}")
                val content = "solana:$url"
                qrCodeGenerator.generateQR(content)
            }
        )

    }

    fun fetchPromos() = viewModelScope.launch(dispatcher) {
        promosConsumer.collectFlow(
            dataRepo.fetchPromos()
        )
    }
    fun createPromo(createPromoArgs: CreatePromoArgs) = viewModelScope.launch(dispatcher) {
        createPromoConsumer.collectFlow(
            dataRepo.createPromo(createPromoArgs)
        )
    }

    fun fetchAppId() = viewModelScope.launch(dispatcher) {
        appIdConsumer.collectFlow(
            dataRepo.fetchAppId()
        )
    }

    init {
//        fetchPromos()
    }

}