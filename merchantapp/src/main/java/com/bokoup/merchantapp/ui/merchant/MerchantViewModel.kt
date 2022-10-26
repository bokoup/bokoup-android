package com.bokoup.merchantapp.ui.merchant

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.TokenAccountListQuery
import com.bokoup.merchantapp.data.DataRepo
import com.bokoup.merchantapp.model.CustomerPayload
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
    private val dataRepo: DataRepo,
    private val remoteDeviceConnector: RemoteDeviceConnector,
    private val barCodeReceiver: BarCodeReceiver,
    private val barcodeScanner: BarcodeScanner
) : ViewModel() {
    var car = CustomActivityRequest("com.bokoup.CUSTOMER_FACING_ACTIVITY", "{\"msg\"=\"Initial...\"}")
    private val _barcodeResult = barCodeReceiver.barcodeString
    val barcodeResult = _barcodeResult.asStateFlow()

    val tokenAccountConsumer = ResourceFlowConsumer<List<TokenAccountListQuery.TokenAccount>>(viewModelScope)

    fun fetchTokenAccounts(owner: String) = viewModelScope.launch(Dispatchers.IO) {
        tokenAccountConsumer.collectFlow(
            dataRepo.fetchTokenAccounts(owner)
        )
    }

    fun startCustomActivity(orderId: String, tokenAccounts: List<TokenAccountListQuery.TokenAccount>, tokenOwner: String) = viewModelScope.launch(Dispatchers.IO) {
        remoteDeviceConnector.resetDevice(ResetDeviceRequest())
        car.payload = Gson().toJson(CustomerPayload(orderId, tokenAccounts, tokenOwner))
        remoteDeviceConnector.startCustomActivity(car, CustomActivityLister())
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


    init {
        Log.d("jingus", barcodeScanner.available.toString())
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