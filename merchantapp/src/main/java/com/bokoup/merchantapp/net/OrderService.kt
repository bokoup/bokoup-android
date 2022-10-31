package com.bokoup.merchantapp.net

import android.util.Log
import com.clover.sdk.v3.order.Discount
import com.clover.sdk.v3.order.Order
import com.clover.sdk.v3.order.OrderConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderService(private val connector: OrderConnector) {
    private val dispatcher = Dispatchers.IO

    suspend fun connect() {
        withContext(dispatcher) {
            val result = connector.connect()
            Log.d("result", result.toString())
        }
    }

    suspend fun disconnect() {
        withContext(dispatcher) {
            connector.disconnect()
        }
    }

    suspend fun connectorStatus(): Boolean {
        return withContext(dispatcher) {
            connector.isConnected
        }
    }

    suspend fun getOrder(orderId: String): Order {
        return withContext(dispatcher) {
            connector.getOrder(orderId)
        }
    }

    suspend fun addDiscount(orderId: String, discount: Discount): Order {
        return withContext(dispatcher) {
            connector.addDiscount(orderId, discount)
        }
    }
}
