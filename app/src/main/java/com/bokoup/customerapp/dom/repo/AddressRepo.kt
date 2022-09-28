package com.bokoup.customerapp.dom.repo


import com.bokoup.customerapp.dom.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepo {
    fun insertAddress(address: Address)
    fun getAddresses(): Flow<List<Address>>
    fun getAddress(id: String): Flow<Address>
}