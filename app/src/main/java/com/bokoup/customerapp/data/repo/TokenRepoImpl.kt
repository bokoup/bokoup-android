package com.bokoup.customerapp.data.repo

import android.util.Log
import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.model.Resource
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.ResourceFlowConsumer
import com.bokoup.customerapp.util.resourceFlowOf
import com.dgsd.ksol.LocalTransactions
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.SolanaSubscription
import com.dgsd.ksol.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.math.sign

class TokenRepoImpl(
    private val tokenDao: TokenDao,
    private val tokenApi: TokenApi,
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
    override fun getApiId(
        mintString: String,
        promoName: String
    ): Flow<Resource<TokenApiId>> =
        resourceFlowOf { tokenApi.getApiId(mintString, promoName) }

    override fun getTokenTransaction(
        mintString: String,
        promoName: String,
        address: String
    ): Flow<Resource<TokenApiResponse>> =
        resourceFlowOf { tokenApi.getTokenTransaction(mintString, promoName, address) }
}