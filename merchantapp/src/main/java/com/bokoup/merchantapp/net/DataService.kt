package com.bokoup.merchantapp.net

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.bokoup.merchantapp.*

class DataService(private val apolloClient: ApolloClient) {

    suspend fun fetchPromos(): List<PromoListQuery.Promo> {
        val response = apolloClient.query(PromoListQuery()).execute()
        return response.data?.promo ?: emptyList()
    }

    suspend fun fetchTokenAccounts(tokenOwner: String, publicKeyString: String): List<TokenAccountListQuery.TokenAccount> {
        val response = apolloClient.query(TokenAccountListQuery(Optional.Present(tokenOwner), Optional.Present(publicKeyString))).execute()
        return response.data?.tokenAccount ?: emptyList()
    }

    val promoSubscription = apolloClient.subscription(PromoListSubscription())
    fun getDelegateTokenSubscription() = apolloClient.subscription(DelegateTokenSubscription())

    suspend fun fetchDelegatedToken(orderId: String, publicKeyString: String): DelegatedTokenQuery.DelegatePromoToken? {
        val response = apolloClient.query(DelegatedTokenQuery(Optional.Present(orderId), Optional.Present(publicKeyString))).execute()
        return response.data?.delegatePromoToken?.firstOrNull()
    }

}