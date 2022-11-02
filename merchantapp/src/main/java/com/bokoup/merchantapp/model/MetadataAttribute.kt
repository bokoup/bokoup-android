package com.bokoup.merchantapp.model

import androidx.compose.ui.text.input.TextFieldValue

data class MetadataAttribute(val trait_type: String, var value: TextFieldValue, val number: Boolean = false)
