package com.bokoup.merchantapp.net

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListQuery
import com.bokoup.merchantapp.PromoListSubscription
import com.bokoup.merchantapp.TokenAccountListQuery

class DataService(private val apolloClient: ApolloClient) {

    suspend fun fetchPromos(): List<PromoListQuery.Promo> {
        val response = apolloClient.query(PromoListQuery()).execute()
        return response?.data?.promo ?: emptyList()
    }

    suspend fun fetchTokenAccounts(tokenOwner: String, promoOwner: String): List<TokenAccountListQuery.TokenAccount> {
        val response = apolloClient.query(TokenAccountListQuery(Optional.Present(tokenOwner), Optional.Present(promoOwner))).execute()
        return response?.data?.tokenAccount ?: emptyList()
    }

    val promoSubscription = apolloClient.subscription(PromoListSubscription())
    fun getDelegateTokenSubscription() = apolloClient.subscription(DelegateTokenSubscription())

}