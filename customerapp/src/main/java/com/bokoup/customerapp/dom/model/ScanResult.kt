package com.bokoup.customerapp.dom.model

import com.google.mlkit.vision.barcode.common.Barcode


sealed interface ScanResult {
    val barcode: Barcode

    data class BokoupUrl(
        val mintString: String,
        val promoName: String,
        override val barcode: Barcode
    ): ScanResult

    data class Other(
        val value: String,
        override val barcode: Barcode
    ): ScanResult
}