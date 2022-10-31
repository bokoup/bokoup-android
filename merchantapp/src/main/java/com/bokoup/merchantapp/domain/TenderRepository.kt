package com.bokoup.merchantapp.domain

import com.bokoup.lib.Resource
import com.bokoup.merchantapp.data.TenderRow
import kotlinx.coroutines.flow.Flow

interface TenderRepository {
    suspend fun connectTenders()
    suspend fun disconnectTenders()

    val isConnected: Boolean
    fun getTenders(): Flow<Resource<List<TenderRow>>>
    fun createTender(
        label: String,
        labelKey: String,
        enabled: Boolean = true,
        opensCashDrawer: Boolean = false
    ): Flow<Resource<Unit>>

    fun deleteTender(tenderId: String): Flow<Resource<Unit>>
    fun setTenderEnabled(tenderId: String, enabled: Boolean): Flow<Resource<Unit>>

}