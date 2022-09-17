package com.bokoup.customerapp.dom.model

import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bokoup.customerapp.R

@Entity
data class Token(
    @PrimaryKey val id: String,
    val title: String,
    val merchant: String,
    val balance: Float,
    val goal: Float
)