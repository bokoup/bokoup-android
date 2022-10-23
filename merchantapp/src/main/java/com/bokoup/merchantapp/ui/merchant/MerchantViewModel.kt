package com.bokoup.merchantapp.ui.merchant

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.merchantapp.data.DataRepo
import com.clover.cfp.connector.*
import com.clover.sdk.v1.Intents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    private val dataRepo: DataRepo,
    private val remoteDeviceConnector: RemoteDeviceConnector
) : ViewModel() {
    private val car = CustomActivityRequest("com.bokoup.CUSTOMER_FACING_ACTIVITY", "{\"msg\"=\"Initial...\"}")

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

    fun startCustomActivity() = viewModelScope.launch(Dispatchers.IO) {
        remoteDeviceConnector.resetDevice(ResetDeviceRequest())
        remoteDeviceConnector.startCustomActivity(car, CustomActivityLister())
    }

    fun endCustomActivity() = viewModelScope.launch(Dispatchers.IO) {
        remoteDeviceConnector.sendMessageToActivity(MessageToActivity(car.name, "FINISH"))
    }
}

class CustomActivityLister : CustomActivityListener {
    override fun onMessageFromActivity(message: MessageFromActivity) {
        Log.d("jingus", message.toString())

    }

    override fun onCustomActivityResult(p0: CustomActivityResponse?) {
        TODO("Not yet implemented")
    }
}