package com.bokoup.merchantapp.model

import android.net.Uri

data class CreatePromoArgs(
    val imageUri: Uri,
    val name: String,
    val symbol: String,
    val description: String,
    val maxMint: String,
    val maxBurn: String,
    val collectionName: String,
    val collectionFamily: String,
    val memo: String
)
