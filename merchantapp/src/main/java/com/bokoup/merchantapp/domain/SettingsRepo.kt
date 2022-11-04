package com.bokoup.merchantapp.domain

import com.bokoup.lib.Resource
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    suspend fun saveKeyPairString(keyPairString: String): Flow<Resource<String>>
    suspend fun saveGroupSeed(groupSeed: String): Flow<Resource<String>>

    suspend fun generateMnemonic(phraseLength: MnemonicPhraseLength = MnemonicPhraseLength.TWENTY_FOUR): Flow<Resource<List<String>>>

    suspend fun getGroupSeed(): Flow<Resource<String>>

    suspend fun getKeyPairfromString(bytesString: String) : KeyPair

    suspend fun getPublicKeyString() : String?
    suspend fun getKeyPairString() : String?
    suspend fun getKeyPair() : KeyPair?
    suspend fun getKeyPairFlow(): Flow<Resource<KeyPair?>>
}