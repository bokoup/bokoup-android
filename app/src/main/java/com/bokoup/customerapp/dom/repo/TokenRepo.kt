package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.dom.model.Token
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
}