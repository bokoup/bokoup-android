package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.AddressDao
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.TokenRepo

class AddressRepoImpl(
    private val addressDao: AddressDao
) : AddressRepo {
    override fun insertAddress(address: Address) = addressDao.insertAddress(address)
    override fun getAddresses() = addressDao.getAddresses()
    override fun getAddress(id: String) = addressDao.getAddress(id)
}