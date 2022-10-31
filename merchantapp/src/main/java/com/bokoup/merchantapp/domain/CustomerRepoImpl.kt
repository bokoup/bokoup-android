package com.bokoup.merchantapp.domain

import android.content.Context
import android.util.Log
import com.bokoup.lib.Resource
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.net.DataService
import com.bokoup.merchantapp.net.OrderService
import com.bokoup.merchantapp.net.TransactionService
import com.clover.sdk.v3.order.Discount
import com.clover.sdk.v3.order.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class CustomerRepoImpl(
    val context: Context,
    private val dataService: DataService,
    private val orderService: OrderService,
    private val txService: TransactionService
) : CustomerRepo {

    // DataService
    override val delegateTokenSubscription =
        dataService.getDelegateTokenSubscription().toFlow().flowOn(Dispatchers.IO)

    // OrderService
    override val orderConnectorStatus: Boolean = false
        suspend fun get() = orderService.connectorStatus()

    override suspend fun connectOrders() {
        orderService.connect()
        Log.d("CustomerRepo:connect", "orderConnector status: $orderConnectorStatus")
    }

    override suspend fun disconnectOrders() {
        orderService.disconnect()
        Log.d("CustomerRepo:disconnect", "orderConnector status: $orderConnectorStatus")
    }

    override fun getOrder(orderId: String): Flow<Resource<Order>> = resourceFlowOf {
        orderService.getOrder(orderId)
    }

    override fun addDiscount(orderId: String, discount: Discount): Flow<Resource<Order>> =
        resourceFlowOf {
            orderService.addDiscount(orderId, discount)
        }


}