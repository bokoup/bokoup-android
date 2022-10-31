package com.bokoup.merchantapp.model

data class CustomerPayload(val orderId: String, val tokenAccounts: List<TokenAccountWithMetadata>, val tokenOwner: String)
