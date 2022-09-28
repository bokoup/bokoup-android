package com.bokoup.customerapp.dom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey val id: String,
    val phrase: String,
    val name: String
)
