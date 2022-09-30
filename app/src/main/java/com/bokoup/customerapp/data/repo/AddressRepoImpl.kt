package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.AddressDao
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.resourceFlowOf
import kotlinx.coroutines.delay

class AddressRepoImpl(
    private val addressDao: AddressDao
) : AddressRepo {
    override fun insertAddress(address: Address) = addressDao.insertAddress(address)
    override fun getAddresses() = resourceFlowOf {
        addressDao.getAddresses()
    }
    override fun getAddress(id: String) = resourceFlowOf { addressDao.getAddress(id) }
    override fun getActiveAddress() = resourceFlowOf { addressDao.getActiveAddress() }
    override fun updateActive(id: String) {
        addressDao.clearActive()
        addressDao.setActive(id)
    }
}