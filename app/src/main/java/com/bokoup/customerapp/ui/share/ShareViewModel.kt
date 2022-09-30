package com.bokoup.customerapp.ui.share

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.util.QRCodeGenerator
import com.bokoup.customerapp.util.ResourceFlowConsumer
import com.bokoup.customerapp.util.SystemClipboard
import com.bokoup.customerapp.util.mapData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val qrCodeGenerator: QRCodeGenerator,
    private val clipboard: SystemClipboard,
    private val addressRepo: AddressRepo,
) : ViewModel() {
    val qrCodeConsumer = ResourceFlowConsumer<Pair<String, Bitmap>>(viewModelScope)

    fun getQrCode() = viewModelScope.launch(Dispatchers.IO) {
        qrCodeConsumer.collectFlow(
            addressRepo.getActiveAddress().mapData { Pair(it.id, qrCodeGenerator.generateQR(it.id)) }
        )

    }
    fun copyToClipboard(contents: String?) {
        if (contents != null) {
            clipboard.copy(contents)
        }
    }

}