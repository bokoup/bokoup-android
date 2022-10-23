package com.bokoup.customerapp.ui.scan

import androidx.lifecycle.ViewModel
import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.dom.model.ScanResult
import com.bokoup.customerapp.util.QRCodeScanner
import com.bokoup.lib.SystemClipboard
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    qrCodeScanner: QRCodeScanner, tokenApi: TokenApi, private val clipboard: SystemClipboard,
) : ViewModel() {
    val scanner = qrCodeScanner.scanner
    private val baseUrl = tokenApi.baseUrl

    val _scanResult = MutableStateFlow<ScanResult?>(null)
    val scanResult = _scanResult.asStateFlow()

    fun parseValue(barcode: Barcode): ScanResult {
        val value = barcode.rawValue
        val protocol = "solana:"
        val isSolana = value?.substring(protocol.indices) == protocol

        return if (isSolana) {
            try {
                val url = URL(value?.substring(protocol.length))
                if (url.host == baseUrl.host && url.path.length > 3) {
                    val path = url.path.split("/")
                    ScanResult.BokoupUrl(
                        action = path[2],
                        mintString = path[3],
                        message = path[4],
                        memo = path.getOrNull(5),
                        barcode = barcode
                    )
                } else {
                    ScanResult.Other(value.toString(), barcode)
                }
            } catch (_: Exception) {
                ScanResult.Other(value.toString(), barcode)
            }
        } else {
            ScanResult.Other(value.toString(), barcode)
        }

    }

    fun getScanResult(barcode: Barcode?) = _scanResult.update { if(barcode != null) {parseValue(barcode)} else {null} }

    fun copyToClipboard(contents: String?) {
        if (contents != null) {
            clipboard.copy(contents)
        }
    }
}

