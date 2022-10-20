package com.bokoup.merchantapp.model

import com.bokoup.merchantapp.PromoListQuery

data class PromoWithMetadataJson(val promo: PromoListQuery.Promo, val image: String, val description: String)
