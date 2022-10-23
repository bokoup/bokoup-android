package com.bokoup.merchantapp.ui.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import com.clover.sdk.v1.app.AppNotificationIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationReceiver(private val context: Context) {

    private val _notification: MutableStateFlow<Notification?> = MutableStateFlow(null)
    val notification = _notification.asStateFlow()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d("jingus", "notification received")
            val event = intent.getStringExtra(AppNotificationIntent.EXTRA_APP_EVENT).toString()
            val payload = intent.getStringExtra(AppNotificationIntent.EXTRA_PAYLOAD).toString()
            _notification.value = Notification(event, payload)
        }
    }

    fun register() {
        Log.d("jingus", "receiver registered")
        val filter = IntentFilter(AppNotificationIntent.ACTION_APP_NOTIFICATION)
        ContextCompat.registerReceiver(context, broadcastReceiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun unregister() {
        Log.d("jingus", "receiver unregistered")
        context.unregisterReceiver(broadcastReceiver)
    }
}

data class Notification(val event: String, val payload: String)