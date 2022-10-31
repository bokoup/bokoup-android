package com.bokoup.merchantapp.domain

import android.util.Log
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.data.TenderDao
import com.bokoup.merchantapp.net.TenderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class TenderRepositoryImpl(
    private val tenderDao: TenderDao,
    private val tenderService: TenderService,
) : TenderRepository {
    override val isConnected: Boolean = false
        suspend fun get() = tenderService.connectorStatus()

    /**
     * This is contrived to include local database.
     * TODO("Fetch tenders directly from Clover without storing in local db.")
     */
    override fun getTenders() = resourceFlowOf {
//        tenderDao.dropTenders()
//        val tenderRows = tenderService.getTenderTypes().map { tender ->
//            TenderRow(
//                tender.id,
//                tender.describeContents(),
//                tender.enabled,
//                tender.label,
//                tender.labelKey,
//                tender.opensCashDrawer,
//                tender.supportsTipping,
//            )
//        }
//        tenderDao.insertAll(tenderRows)
        tenderDao.getTenders()
    }.flowOn(Dispatchers.IO)

    override fun createTender(
        label: String,
        labelKey: String,
        enabled: Boolean,
        opensCashDrawer: Boolean,
    ) = resourceFlowOf {
        tenderService.createTender(label, labelKey, enabled, opensCashDrawer)
    }

    override fun deleteTender(tenderId: String) = resourceFlowOf {
        tenderService.deleteTender(tenderId)
    }

    override fun setTenderEnabled(tenderId: String, enabled: Boolean) = resourceFlowOf {
        tenderService.setTenderEnabled(tenderId, enabled)
    }

    override suspend fun connectTenders() {
        tenderService.connect()
        Log.d("jingus", "tender connector status: ${isConnected.toString()}")
    }

    override suspend fun disconnectTenders() {
        tenderService.disconnect()
        Log.d("jingus", "tender connector status: ${isConnected.toString()}")
    }
}