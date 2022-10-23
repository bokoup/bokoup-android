package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.data.DataRepo
import com.bokoup.merchantapp.data.SolanaRepo
import com.clover.sdk.v1.Intents
import com.dgsd.ksol.model.TransactionSignature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator,
    private val notificationReceiver: NotificationReceiver,
    private val nfcReceiver: NFCReceiver,
    private val solanaRepo: SolanaRepo,
    private val dataRepo: DataRepo
) : ViewModel() {
    val qrCodeConsumer = ResourceFlowConsumer<Bitmap>(viewModelScope)
    val notification = notificationReceiver.notification
    val _signatureSubscription: MutableStateFlow<TransactionSignature?> = MutableStateFlow(null)
    val signatureSubscription = _signatureSubscription.asStateFlow()

    val delegateTokenSubscription = dataRepo.delegateTokenSubscriptionFlow

    fun getQrCode(orderId: String) = viewModelScope.launch(Dispatchers.IO) {
        val mintString: String = "4oEvwvZkHdrRJFHSnpnrVCxaibeUVKqAkiTJEfXXNfiD"
            val message: String = "Approve to delegate Promo 2"
        val memo: String = orderId
        qrCodeConsumer.collectFlow(
            resourceFlowOf {
                val url = URL("http://99.91.8.130:8080/promo/delegate/$mintString/${URLEncoder.encode(message, "utf-8")}/${URLEncoder.encode(memo, "utf-8")}")
                val content = "solana:$url"
                Log.d("jing", content)
                qrCodeGenerator.generateQR(content)
            }
        )
    }

    fun registerNotificationReceiver() {
        notificationReceiver.register()
    }

    fun unregisterNotificationReceiver() {
        notificationReceiver.unregister()
    }

    fun registerNFCReceiver() {
        nfcReceiver.register()
    }

    fun unregisterNFCReceiver() {
        nfcReceiver.unregister()
    }
    fun cancel(activity: Activity) {
        val data = Intent()
        data.putExtra(Intents.EXTRA_DECLINE_REASON, "Cancelled!")
        activity.setResult(RESULT_CANCELED, data)
        activity.finish()
    }

    fun approve(activity: Activity) {
        val data = Intent()
        data.putExtra(Intents.EXTRA_AMOUNT, 21.toLong());
        data.putExtra(Intents.EXTRA_CLIENT_ID, 1234.toString());
        data.putExtra(Intents.EXTRA_NOTE, "Transaction Id: blah");
        activity.setResult(RESULT_OK, data);
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