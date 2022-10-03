package com.bokoup.customerapp.dom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Token(
    @PrimaryKey val id: String,
    val title: String,
    val merchant: String,
    val balance: Float,
    val goal: Float
)