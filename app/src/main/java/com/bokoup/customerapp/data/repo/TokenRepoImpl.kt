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
    private val localTransactions: LocalTransactions,
    private val solanaApi: SolanaApi,
) : TokenRepo {
    val subscriptionService = solanaApi.createSubscription()
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

    override fun signAndSend(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>> =
        flow<Resource<TransactionSignature>> {
            emit(Resource.Loading())
            runCatching {
                val localTransaction = localTransactions.deserializeTransaction(transaction)
                solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
            }.onSuccess {signature ->
                subscriptionService.connect()
                subscriptionService.signatureSubscribe(signature, Commitment.CONFIRMED).collect {
                    try {
                        if(it is TransactionSignatureStatus.Confirmed) {
                            subscriptionService.disconnect()
                            emit(Resource.Success(signature))
                        }
                    } catch(error: Throwable) {
                        subscriptionService.disconnect()
                        emit(Resource.Error(error))

                    }
                }
            }.onFailure {
                emit(Resource.Error(it))
            }
        }.flowOn(Dispatchers.IO)
}