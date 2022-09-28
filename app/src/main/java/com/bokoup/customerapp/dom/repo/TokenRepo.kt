package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
    suspend fun getApiId(mintString: String, promoName: String): TokenApiId
    suspend fun getTokenTransaction(mintString: String, promoName: String, address: String): TokenApiResponse
}