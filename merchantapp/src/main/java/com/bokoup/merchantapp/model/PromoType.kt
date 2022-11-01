package com.bokoup.merchantapp.model

enum class PromoType(val title: String, val attributes: List<MetadataAttribute>) {
    buyXProductGetYFree(
        title = "buyXProductGetYFree",
        attributes = listOf(
            MetadataAttribute("productId", ""),
            MetadataAttribute("buyXProduct", "", true),
            MetadataAttribute("getYProduct", "", true),
        )
    ),
    buyXCurrencyGetYPercent(
        title = "buyXCurrencyGetYPercent",
        attributes = listOf(
            MetadataAttribute("buyXCurrency", "", true),
            MetadataAttribute("getYPercent", "", true),
        )
    );
}