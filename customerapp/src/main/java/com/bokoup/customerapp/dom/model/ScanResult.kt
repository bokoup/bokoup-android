package com.bokoup.customerapp.dom.model

import com.google.mlkit.vision.barcode.common.Barcode


sealed interface ScanResult {
    val barcode: Barcode

    data class BokoupUrl(
        val url: String,
        override val barcode: Barcode
    ): ScanResult

    data class Other(
        val value: String,
        override val barcode: Barcode
    ): ScanResult
}