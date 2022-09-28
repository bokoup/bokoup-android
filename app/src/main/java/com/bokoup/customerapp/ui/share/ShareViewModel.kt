package com.bokoup.customerapp.ui.share

import android.graphics.Bitmap
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.util.QRCodeGenerator
import com.bokoup.customerapp.util.SystemClipboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator,
    private val clipboard: SystemClipboard
) : ViewModel() {
    var qRCode: Bitmap? by mutableStateOf(null)

    suspend fun getQrCode(contents: String) = viewModelScope.launch {
        qRCode = qrCodeGenerator.generateQR(contents)
    }

    fun copyToClipboard(contents: String, channel: Channel<String>) {
        clipboard.copy(contents)
        channel.trySend("Address copied to clipboard!")
    }

}