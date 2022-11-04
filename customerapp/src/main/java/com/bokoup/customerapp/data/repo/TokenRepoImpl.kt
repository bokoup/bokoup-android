package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.TransactionService
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.lib.Resource
import com.bokoup.lib.resourceFlowOf
import kotlinx.coroutines.flow.Flow

class TokenRepoImpl(
    private val tokenDao: TokenDao,
    private val transactionService: TransactionService,
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
    override fun getApiId(
        url: String,
    ): Flow<Resource<TokenApiId>> =
        resourceFlowOf { transactionService.getApiId(url) }

    override fun getTokenTransaction(
        url: String,
        address: String
    ): Flow<Resource<TokenApiResponse>> =
        resourceFlowOf { transactionService.getTokenTransaction(url, address) }
}