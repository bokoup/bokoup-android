package com.bokoup.merchantapp.model

sealed class SharedPrefKeys {
    object KeyPairString : SharedPrefKeys()
    object PublicKeyString : SharedPrefKeys()
    object GroupSeed : SharedPrefKeys()
}

val SharedPrefKeys.key: String
    get() = this.javaClass.simpleName.replaceFirstChar { it.lowercase() }
