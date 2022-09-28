package com.bokoup.customerapp.ui.scan

import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.util.QRCodeScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val qrCodeScanner: QRCodeScanner,
) : ViewModel() {
    val scanner = qrCodeScanner.scanner
}