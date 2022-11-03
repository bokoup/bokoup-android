package com.bokoup.merchantapp.ui.promo

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.domain.PromoRepo
import com.bokoup.merchantapp.domain.SettingsRepo
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoResult
import com.bokoup.merchantapp.model.PromoType
import com.bokoup.merchantapp.model.PromoWithMetadata
import com.dgsd.ksol.core.model.KeyPair
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
    private val promoRepo: PromoRepo,
    private val qrCodeGenerator: QRCodeGenerator,
    private val settingsRepo: SettingsRepo
) : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    val promoSubscription = promoRepo.promoSubscriptionFlow

    val promosConsumer = ResourceFlowConsumer<List<PromoWithMetadata>>(viewModelScope)
    val appIdConsumer = ResourceFlowConsumer<AppId>(viewModelScope)
    val createPromoConsumer = ResourceFlowConsumer<CreatePromoResult>(viewModelScope)
    val groupSeedConsumer = ResourceFlowConsumer<String>(viewModelScope)
    val keyPairConsumer = ResourceFlowConsumer<KeyPair?>(viewModelScope)
    val signatureConsumer = ResourceFlowConsumer<String>(viewModelScope)

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
                val url = URL(
                    "http://99.91.8.130:8080/promo/mint/$mintString/${
                        URLEncoder.encode(
                            message,
                            "utf-8"
                        )
                    }"
                )
                val content = "solana:$url"
                qrCodeGenerator.generateQR(content)
            }
        )

    }

    fun getGroupSeed() = viewModelScope.launch(dispatcher) {
        groupSeedConsumer.collectFlow(
            settingsRepo.getGroupSeed()
        )
    }

    fun getKeyPair() = viewModelScope.launch(dispatcher) {
        keyPairConsumer.collectFlow(
            settingsRepo.getKeyPairFlow()
        )
    }
    fun fetchPromos() = viewModelScope.launch(dispatcher) {
        promosConsumer.collectFlow(
            promoRepo.fetchPromos()
        )
    }

    fun createPromo(promo: PromoType, uri: Uri, memo: String?, payer: String, groupSeed: String) =
        viewModelScope.launch(dispatcher) {
            createPromoConsumer.collectFlow(
                promoRepo.createPromo(promo, uri, memo, payer, groupSeed)
            )
        }

    fun signAndSend(transaction: String, keyPair: KeyPair) =
        viewModelScope.launch(dispatcher) {
            signatureConsumer.collectFlow(
                promoRepo.signAndSend(transaction, keyPair)
            )
        }

    fun fetchAppId() = viewModelScope.launch(dispatcher) {
        appIdConsumer.collectFlow(
            promoRepo.fetchAppId()
        )
    }

}