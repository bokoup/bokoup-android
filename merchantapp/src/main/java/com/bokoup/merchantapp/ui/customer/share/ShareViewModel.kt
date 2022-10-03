package com.bokoup.merchantapp.ui.customer.share

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.resourceFlowOf
import com.clover.sdk.v1.Intents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator,
) : ViewModel() {
    val qrCodeConsumer = ResourceFlowConsumer<Pair<String, Bitmap>>(viewModelScope)

    fun getQrCode() = viewModelScope.launch(Dispatchers.IO) {
        qrCodeConsumer.collectFlow(
            resourceFlowOf {
                val url = URL("https://demo-api-6jgxmo2doq-uw.a.run.app/promo/9cppW5ugbEHygEicY8vWcgyCRNqkbdTiwjqtBDpH7913/Promo2")
                val content = "solana:${URLEncoder.encode(url.toString(), "utf-8")}"
                Pair("bokoup", qrCodeGenerator.generateQR(content))
            }
        )

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

}