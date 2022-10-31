package com.bokoup.merchantapp.model

import com.bokoup.merchantapp.PromoListQuery

data class PromoWithMetadata(val promo: PromoListQuery.Promo, val image: String, val description: String, val attributes: Map<String, String>)
