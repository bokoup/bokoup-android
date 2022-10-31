package com.bokoup.merchantapp.domain

import com.bokoup.lib.Resource
import com.dgsd.ksol.SolanaSubscription
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.core.model.Lamports
import com.dgsd.ksol.core.model.PublicKey
import com.dgsd.ksol.core.model.TransactionSignature
import kotlinx.coroutines.flow.Flow

interface SolanaRepo {
    val solanaSubscription: SolanaSubscription

    fun signAndSend(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>>

//    fun signatureSubscribe(signature: TransactionSignature, )

    suspend fun airDrop(accountKey: PublicKey, lamports: Lamports = 2_000_000_000): TransactionSignature
}