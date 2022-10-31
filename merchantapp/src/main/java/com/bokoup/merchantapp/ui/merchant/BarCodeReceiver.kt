package com.bokoup.merchantapp.ui.merchant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import com.clover.sdk.v3.scanner.BarcodeResult
import kotlinx.coroutines.flow.MutableStateFlow

class BarCodeReceiver(private val context: Context) {

    val barcodeString: MutableStateFlow<String?> = MutableStateFlow(null)

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d("jingus", "barcode received")
            val barcodeResult = BarcodeResult(intent)
            if (barcodeResult.isBarcodeAction) {
                barcodeString.value = barcodeResult.barcode
            }
        }
    }

    fun register() {
        Log.d("jingus", "receiver registered")
        val filter = IntentFilter(BarcodeResult.INTENT_ACTION)
        ContextCompat.registerReceiver(context, broadcastReceiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun unregister() {
        Log.d("jingus", "receiver unregistered")
        context.unregisterReceiver(broadcastReceiver)
    }
}

// https://clover.github.io/clover-android-sdk/com/clover/sdk/v1/Intents.html#ACTION_PAYMENT_PROCESSED