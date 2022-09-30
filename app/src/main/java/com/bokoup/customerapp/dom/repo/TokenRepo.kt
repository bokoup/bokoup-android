package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Resource
import com.bokoup.customerapp.dom.model.Token
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import com.dgsd.ksol.model.TransactionSignature
import com.dgsd.ksol.model.TransactionSignatureStatus
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
    fun getApiId(mintString: String, promoName: String): Flow<Resource<TokenApiId>>
    fun getTokenTransaction(
        mintString: String,
        promoName: String,
        address: String
    ): Flow<Resource<TokenApiResponse>>
    fun signAndSend(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>>
}