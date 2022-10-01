package com.bokoup.customerapp.dom.repo

import com.bokoup.customerapp.dom.model.Resource
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.Lamports
import com.dgsd.ksol.model.PublicKey
import com.dgsd.ksol.model.TransactionSignature
import kotlinx.coroutines.flow.Flow

interface SolanaRepo {
    fun signAndSend(
        transaction: String,
        keyPair: KeyPair
    ): Flow<Resource<TransactionSignature>>

    suspend fun airDrop(accountKey: PublicKey, lamports: Lamports = 2_000_000_000): TransactionSignature
}