package com.bokoup.merchantapp.data

import com.apollographql.apollo3.ApolloClient
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListQuery
import com.bokoup.merchantapp.PromoListSubscription

class DataService(private val apolloClient: ApolloClient) {

    suspend fun fetchPromos(): List<PromoListQuery.Promo> {
        val response = apolloClient.query(PromoListQuery()).execute()
        return response?.data?.promo?.filterNotNull() ?: emptyList<PromoListQuery.Promo>()
    }

    val promoSubscription = apolloClient.subscription(PromoListSubscription())
    val delegateTokenSubscription = apolloClient.subscription(DelegateTokenSubscription())
}