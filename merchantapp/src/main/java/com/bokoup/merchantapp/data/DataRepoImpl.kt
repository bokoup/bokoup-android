package com.bokoup.merchantapp.data

import com.bokoup.lib.resourceFlowOf

class DataRepoImpl(
    private val dataService: DataService,
    private val transactionService: TransactionService
): DataRepo {
    override fun fetchPromos() = resourceFlowOf { dataService.fetchPromos() }
    override fun fetchAppId() = resourceFlowOf { transactionService.service.mintPromoToken() }
}