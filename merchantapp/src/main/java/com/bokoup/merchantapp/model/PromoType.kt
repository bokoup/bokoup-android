package com.bokoup.merchantapp.model

import com.bokoup.merchantapp.util.PromoTypeSerializer
import com.google.gson.GsonBuilder

sealed interface BasePromo {
    val name: String
    val symbol: String
    val description: String
    val collectionName: String
    val collectionFamily: String
    val maxMint: Int?
    val maxBurn: Int?
}

sealed interface PromoType {
    class BuyXProductGetYFree(
        override val name: String,
        override val symbol: String,
        override val description: String,
        override val collectionName: String,
        override val collectionFamily: String,
        override val maxMint: Int?,
        override val maxBurn: Int?,
        val productId: String,
        val buyXProduct: Int,
        val getYProduct: Int,
    ) : PromoType, BasePromo

    data class BuyXCurrencyGetYPercent(
        override val name: String,
        override val symbol: String,
        override val description: String,
        override val collectionName: String,
        override val collectionFamily: String,
        override val maxMint: Int?,
        override val maxBurn: Int?,
        val buyXCurrency: Int,
        val getYPercent: Int,
    ) : PromoType, BasePromo
}


val PromoType.asJson: String
    get() {
        val gson = GsonBuilder().apply {
            registerTypeAdapter(PromoType::class.java, PromoTypeSerializer())
        }.create()
        return gson.toJson(this, PromoType::class.java)
    }


