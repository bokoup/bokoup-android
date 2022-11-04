package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Token
import com.bokoup.lib.Resource
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
    fun getApiId(
        url: String,
    ): Flow<Resource<TokenApiId>>

    fun getTokenTransaction(
        url: String,
        address: String
    ): Flow<Resource<TokenApiResponse>>

}