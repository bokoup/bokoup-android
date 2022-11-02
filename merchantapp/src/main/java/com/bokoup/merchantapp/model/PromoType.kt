package com.bokoup.merchantapp.model

import android.net.Uri

sealed interface BasePromo {
    val uri: Uri
    val name: String
    val symbol: String
    val description: String
    val collectionName: String
    val collectionFamily: String
    val maxMint: Int?
    val maxBurn: Int?
    val memo: String?
}

sealed interface PromoType {
    class BuyXProductGetYFree(
        override val uri: Uri,
        override val name: String,
        override val symbol: String,
        override val description: String,
        override val collectionName: String,
        override val collectionFamily: String,
        override val maxMint: Int?,
        override val maxBurn: Int?,
        override val memo: String?,
        val productId: String,
        val buyXProduct: Int,
        val getYProduct: Int,
    ) : PromoType, BasePromo
    data class BuyXCurrencyGetYPercent(
        override val uri: Uri,
        override val name: String,
        override val symbol: String,
        override val description: String,
        override val collectionName: String,
        override val collectionFamily: String,
        override val maxMint: Int?,
        override val maxBurn: Int?,
        override val memo: String?,
        val buyXCurrency: Int,
        val getYPercent: Int,
    ) : PromoType, BasePromo
}

