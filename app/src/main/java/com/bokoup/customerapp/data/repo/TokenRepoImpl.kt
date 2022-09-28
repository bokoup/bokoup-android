package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.dgsd.ksol.LocalTransactions
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import com.dgsd.ksol.model.TransactionSignature

class TokenRepoImpl(
    private val tokenDao: TokenDao,
    private val tokenApi: TokenApi,
    private val localTransactions: LocalTransactions,
    private val solanaApi: SolanaApi
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
    override suspend fun getApiId(mintString: String, promoName: String) = tokenApi.getApiId(mintString, promoName)

    override suspend fun getTokenTransaction(
        mintString: String,
        promoName: String,
        address: String
    ): TokenApiResponse = tokenApi.getTokenTransaction(mintString, promoName, address)

    override fun deserializeTransaction(transaction: String): LocalTransaction {
        return localTransactions.deserializeTransaction(transaction)
    }
    override suspend fun signAndSend(
        localTransaction: LocalTransaction,
        keyPair: KeyPair
    ): TransactionSignature {
        return solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
    }
}