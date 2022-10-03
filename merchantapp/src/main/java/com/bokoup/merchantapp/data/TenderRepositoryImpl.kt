package com.bokoup.merchantapp.data

import android.util.Log
import com.bokoup.lib.resourceFlowOf

class TenderRepositoryImpl(
    private val tenderDao: TenderDao,
    private val tenderService: CloverService,
) : TenderRepository {
    override val isConnected: Boolean = false
        suspend fun get() = tenderService.tenderConnectorStatus()

    /**
     * This is contrived to include local database.
     * TODO("Fetch tenders directly from Clover without storing in local db.")
     */
    override fun getTenders() = resourceFlowOf {
        tenderDao.dropTenders()
        val tenderRows = tenderService.getTenderTypes().map { tender ->
            TenderRow(
                tender.id,
                tender.describeContents(),
                tender.enabled,
                tender.label,
                tender.labelKey,
                tender.opensCashDrawer,
                tender.supportsTipping,
            )
        }
        tenderDao.insertAll(tenderRows)
        tenderDao.getTenders()
    }

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

    override suspend fun disconnectTenders() {
        tenderService.disconnectTenders()
        Log.d("jingus", "tender connector status: ${isConnected.toString()}")
    }
}