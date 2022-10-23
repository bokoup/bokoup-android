package com.bokoup.merchantapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.clover.sdk.v1.app.AppNotificationIntent.EXTRA_PAYLOAD

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(TAG, log)
                Log.d(TAG, intent.getStringExtra(EXTRA_PAYLOAD).toString())
                Toast.makeText(context, log, Toast.LENGTH_LONG).show()
            }
        }
    }
}
