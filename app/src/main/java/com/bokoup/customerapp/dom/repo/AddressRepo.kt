package com.bokoup.customerapp.dom.repo


import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.model.Resource
import kotlinx.coroutines.flow.Flow

interface AddressRepo {
    suspend fun insertAddressPrep(active: Boolean? = null)
    fun insertAddress(active: Boolean? = null): Flow<Resource<Unit>>
    fun getAddresses(): Flow<Resource<List<Address>>>
    fun getAddress(id: String): Flow<Resource<Address>>
    fun getActiveAddress(): Flow<Resource<Address>>

    fun updateActive(id: String)
}