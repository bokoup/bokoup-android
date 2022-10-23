package com.bokoup.customerapp.dom.repo

import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.customerapp.TokenAccountListSubscription
import com.bokoup.lib.Resource
import kotlinx.coroutines.flow.Flow

interface DataRepo {
    val tokenAccountSubscriptionFlow: Flow<ApolloResponse<TokenAccountListSubscription.Data>>

    fun getActiveAddress(): Flow<Resource<String>>
}