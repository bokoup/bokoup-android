package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.repo.TokenRepo

class TokenRepoImpl(
    private val tokenDao: TokenDao,
    private val tokenApi: TokenApi
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
    override suspend fun getApiId(mintString: String, promoName: String) = tokenApi.getApiId(mintString, promoName)

    override suspend fun getTokenTransaction(
        mintString: String,
        promoName: String,
        address: String
    ) = tokenApi.getTokenTransaction(mintString, promoName, address)
}