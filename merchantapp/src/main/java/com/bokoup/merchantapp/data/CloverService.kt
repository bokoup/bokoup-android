package com.bokoup.merchantapp.data

import com.clover.sdk.v1.tender.Tender
import com.clover.sdk.v1.tender.TenderConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CloverService(private val tenderConnector: TenderConnector) {
    private val dispatcher = Dispatchers.Default

    init {
        tenderConnector.connect()
    }

    suspend fun getTenderTypes(): List<Tender> {
        return withContext(dispatcher) {
            tenderConnector.tenders
        }
    }


    suspend fun disconnectTenders() {
        withContext(dispatcher) {
            tenderConnector.disconnect()
        }
    }

    suspend fun tenderConnectorStatus(): Boolean {
        return withContext(dispatcher) {
            tenderConnector.isConnected
        }
    }

    suspend fun createTender(label: String, labelKey: String, enabled: Boolean = true, opensCashDrawer: Boolean = false) {
        withContext(dispatcher) {
            tenderConnector.checkAndCreateTender(label, labelKey, enabled, opensCashDrawer)
        }
    }

    suspend fun deleteTender(tenderId: String) {
        withContext(dispatcher) {
            tenderConnector.deleteTender(tenderId)
            delay(1000)
        }
    }

    suspend fun setTenderEnabled(tenderId: String, enabled: Boolean) {
        withContext(dispatcher) {
            tenderConnector.setEnabled(tenderId, enabled)
            delay(1000)
        }
    }
}
