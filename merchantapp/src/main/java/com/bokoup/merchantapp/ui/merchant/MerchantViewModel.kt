package com.bokoup.merchantapp.ui.merchant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.domain.PromoRepo
import com.bokoup.merchantapp.model.CustomerPayload
import com.bokoup.merchantapp.model.TokenAccountWithMetadata
import com.clover.cfp.connector.*
import com.clover.sdk.v1.Intents
import com.clover.sdk.v3.scanner.BarcodeScanner
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    private val promoRepo: PromoRepo,
    private val remoteDeviceConnector: RemoteDeviceConnector,
    private val barCodeReceiver: BarCodeReceiver,
    private val barcodeScanner: BarcodeScanner
) : ViewModel() {
    var car = CustomActivityRequest("com.bokoup.CUSTOMER_FACING_ACTIVITY", "{\"msg\"=\"Initial...\"}")
    private val _barcodeResult = barCodeReceiver.barcodeString
    val barcodeResult = _barcodeResult.asStateFlow()
    val tokenAccountConsumer = ResourceFlowConsumer<List<TokenAccountWithMetadata>>(viewModelScope)
    val customerActivityResult: MutableState<CustomActivityResponse?> = mutableStateOf(null)

    fun fetchEligibleTokenAccounts(tokenOwner: String, promoOwner: String, orderId: String) = viewModelScope.launch(Dispatchers.IO) {
        tokenAccountConsumer.collectFlow(
            promoRepo.fetchEligibleTokenAccounts(tokenOwner, promoOwner, orderId)
        )
    }

    fun startCustomActivity(orderId: String, tokenAccounts: List<TokenAccountWithMetadata>, tokenOwner: String) = viewModelScope.launch(Dispatchers.IO) {
        remoteDeviceConnector.resetDevice(ResetDeviceRequest())
        car.payload = Gson().toJson(CustomerPayload(orderId, tokenAccounts, tokenOwner))
        remoteDeviceConnector.startCustomActivity(car, CustomerActivityListener(customerActivityResult))
    }

    fun endCustomActivity() = viewModelScope.launch(Dispatchers.IO) {
        remoteDeviceConnector.sendMessageToActivity(MessageToActivity(car.name, "FINISH"))
    }
    fun startBarcodeScanner() = viewModelScope.launch(Dispatchers.IO) {
        barCodeReceiver.register()
        _barcodeResult.value = null
        val extras = Bundle()
        extras.putInt(Intents.EXTRA_SCANNER_FACING, BarcodeScanner.BARCODE_SCANNER_FACING_CUSTOMER)
        extras.putBoolean(Intents.EXTRA_SCAN_1D_CODE, true)
        extras.putBoolean(Intents.EXTRA_SCAN_QR_CODE, true)
        barcodeScanner.executeStartScan(extras)
    }
    fun stopBarcodeScanner() = viewModelScope.launch(Dispatchers.IO) {
        barcodeScanner.executeStopScan(null)
        barCodeReceiver.unregister()
    }

    fun approve(activity: Activity) {
        activity.setResult(Activity.RESULT_OK)
        activity.finish();
    }
}

class CustomerActivityListener(private val customerActivityResult: MutableState<CustomActivityResponse?>) : CustomActivityListener {

    override fun onMessageFromActivity(message: MessageFromActivity) {
        Log.d("onMessageFromActivity", message.payload.toString())

    }

    override fun onCustomActivityResult(p0: CustomActivityResponse) {
        customerActivityResult.value = p0
        Log.d("onCustomActivityResult", Gson().toJson(p0))
    }
}