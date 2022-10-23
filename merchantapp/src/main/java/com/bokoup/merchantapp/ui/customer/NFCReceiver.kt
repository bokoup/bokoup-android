package com.bokoup.merchantapp.ui.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.clover.sdk.v1.app.AppNotificationIntent

private const val TAG = "NFCReciever"

class NFCReceiver(private val context: Context) {

//    private val _notification: MutableStateFlow<Notification?> = MutableStateFlow(null)
//    val notification = _notification.asStateFlow()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            StringBuilder().apply {
                append("Action: ${intent.action}\n")
                append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
                toString().also { log ->
                    Log.d(TAG, log)
                    Log.d(TAG, intent.getStringExtra(AppNotificationIntent.EXTRA_PAYLOAD).toString())
                    Toast.makeText(context, log, Toast.LENGTH_LONG).show()
                }
            }

//            Log.d("jingus", "notification received")
//            val event = intent.getStringExtra(AppNotificationIntent.EXTRA_APP_EVENT).toString()
//            val payload = intent.getStringExtra(AppNotificationIntent.EXTRA_PAYLOAD).toString()
//            _notification.value = Notification(event, payload)
        }
    }

    fun register() {
        Log.d(TAG, "receiver registered")
        val filter = IntentFilter("android.nfc.action.NDEF_DISCOVERED")
        ContextCompat.registerReceiver(context, broadcastReceiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun unregister() {
        Log.d(TAG, "receiver unregistered")
        context.unregisterReceiver(broadcastReceiver)
    }
}

//data class Notification(val event: String, val payload: String)