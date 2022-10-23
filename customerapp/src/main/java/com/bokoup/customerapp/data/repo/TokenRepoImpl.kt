package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.lib.Resource
import com.bokoup.lib.resourceFlowOf
import kotlinx.coroutines.flow.Flow

class TokenRepoImpl(
    private val tokenDao: TokenDao,
    private val tokenApi: TokenApi,
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
    override fun getApiId(
        action: String,
        mintString: String,
        promoName: String,
        memo: String?
    ): Flow<Resource<TokenApiId>> =
        resourceFlowOf { tokenApi.getApiId(action, mintString, promoName, memo) }

    override fun getTokenTransaction(
        action: String,
        mintString: String,
        message: String,
        memo: String?,
        address: String
    ): Flow<Resource<TokenApiResponse>> =
        resourceFlowOf { tokenApi.getTokenTransaction(action, mintString, message, memo, address) }
}