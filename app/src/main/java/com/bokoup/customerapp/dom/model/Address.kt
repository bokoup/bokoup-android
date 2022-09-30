package com.bokoup.customerapp.dom.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.model.KeyPair

@Entity(indices = [Index(value = ["active"], unique = true)])
data class Address(
    @PrimaryKey val id: String,
    val phrase: String,
    val name: String? = null,
    val active: Boolean? = null
)
suspend fun Address.toKeyPair(): KeyPair =
    KeyFactory.createKeyPairFromMnemonic(phrase.split(", "))