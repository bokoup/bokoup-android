package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Token
import com.bokoup.lib.Resource
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
    fun getApiId(
        action: String,
        mintString: String,
        promoName: String,
        memo: String?
    ): Flow<Resource<TokenApiId>>

    fun getTokenTransaction(
        action: String,
        mintString: String,
        message: String,
        memo: String?,
        address: String
    ): Flow<Resource<TokenApiResponse>>

}