package com.bokoup.customerapp.data.net

import com.apollographql.apollo3.ApolloClient
import com.bokoup.customerapp.TokenAccountListSubscription

class DataService(private val apolloClient: ApolloClient) {
    val tokenAccountSubscription = apolloClient.subscription(TokenAccountListSubscription())
}