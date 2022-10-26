package com.bokoup.merchantapp.model

import com.bokoup.merchantapp.TokenAccountListQuery

data class CustomerPayload(val orderId: String, val tokenAccounts: List<TokenAccountListQuery.TokenAccount>, val tokenOwner: String)
