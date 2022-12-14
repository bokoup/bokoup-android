package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.dom.repo.SolanaRepo
import com.bokoup.lib.Resource
import com.bokoup.lib.resourceFlowOf
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.SolanaSubscription
import com.dgsd.ksol.core.LocalTransactions
import com.dgsd.ksol.core.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SolanaRepoImpl(
    private val solanaApi: SolanaApi,
    private val localTransactions: LocalTransactions,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SolanaRepo {
    override val solanaSubscription: SolanaSubscription = solanaApi.createSubscription()
    override fun signAndSendWithSubscription(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>> =
        flow<Resource<TransactionSignature>> {
            emit(Resource.Loading())
            runCatching {
                val localTransaction = localTransactions.deserializeTransaction(transaction)
                solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
            }.onSuccess { signature ->
                solanaSubscription.connect()
                solanaSubscription.signatureSubscribe(signature, Commitment.CONFIRMED).collect {
                    try {
                        if (it is TransactionSignatureStatus.Confirmed) {
                            solanaSubscription.disconnect()
                            emit(Resource.Success(signature))
                        }
                    } catch (error: Throwable) {
                        solanaSubscription.disconnect()
                        emit(Resource.Error(error))

                    }
                }
            }.onFailure {
                emit(Resource.Error(it))
            }
        }.flowOn(Dispatchers.IO)

    override fun signAndSend(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>> = resourceFlowOf {
        val localTransaction = localTransactions.deserializeTransaction(transaction)
        solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
    }

    override suspend fun airDrop(accountKey: PublicKey, lamports: Lamports): TransactionSignature =
        withContext(dispatcher) {
            solanaApi.requestAirdrop(accountKey, lamports)
        }
}