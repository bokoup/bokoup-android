package com.bokoup.merchantapp.domain

import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.lib.Resource
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.clover.sdk.v3.order.Discount
import com.clover.sdk.v3.order.Order
import kotlinx.coroutines.flow.Flow

// gq https://shining-sailfish-15.hasura.app/v1/graphql --introspect > schema.graphql

interface CustomerRepo {

    // DataService
    val delegateTokenSubscription: Flow<ApolloResponse<DelegateTokenSubscription.Data>>

    // OrderService
    val orderConnectorStatus: Boolean
    suspend fun connectOrders()
    suspend fun disconnectOrders()
    fun getOrder(orderId: String): Flow<Resource<Order>>
    fun addDiscount(orderId: String, discount: Discount): Flow<Resource<Order>>

}