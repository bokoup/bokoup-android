package com.bokoup.merchantapp.data

import com.bokoup.lib.Resource
import kotlinx.coroutines.flow.Flow

interface TenderRepository {
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
    suspend fun disconnectTenders()
}