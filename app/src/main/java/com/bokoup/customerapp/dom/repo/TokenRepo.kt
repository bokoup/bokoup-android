package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Token
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import com.dgsd.ksol.model.TransactionSignature
import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    fun getTokensFromRoom(): Flow<List<Token>>
    suspend fun getApiId(mintString: String, promoName: String): TokenApiId
    suspend fun getTokenTransaction(mintString: String, promoName: String, address: String): TokenApiResponse
    fun deserializeTransaction(transaction: String): LocalTransaction
    suspend fun signAndSend(localTransaction: LocalTransaction, keyPair: KeyPair): TransactionSignature
}