package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.AddressDao
import com.bokoup.customerapp.data.net.DataService
import com.bokoup.customerapp.dom.repo.DataRepo
import com.bokoup.lib.resourceFlowOf

class DataRepoImpl(private val dataService: DataService, private val addressDao: AddressDao): DataRepo {
    override val tokenAccountSubscriptionFlow = dataService.tokenAccountSubscription.toFlow()

    override fun getActiveAddress() = resourceFlowOf { addressDao.getActiveAddress().id }
}