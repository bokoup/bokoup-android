package com.bokoup.merchantapp.domain

import com.bokoup.lib.Resource
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    /**
     * Saves a [com.dgsd.ksol.core.model.KeyPair] created from a mnemonic to SharedPreferences
     * storage and returns the PublicKey address as a [String].
     */
    suspend fun saveMnemonic(mnemonic: List<String>) : Flow<Resource<String>>

    /**
     * Generates a mnemonic phrase of the specifed length.
     */
    suspend fun generateMnemonic(phraseLength: MnemonicPhraseLength = MnemonicPhraseLength.TWENTY_FOUR) : Flow<Resource<List<String>>>

    /**
     * Returns a [com.dgsd.ksol.core.model.KeyPair] from mnemonic phrase stored in SharedPreferences.
     */
    suspend fun getMnemonic() : Flow<Resource<List<String>>>

    /**
     * Returns a [com.dgsd.ksol.core.model.KeyPair] from mnemonic phrase stored in SharedPreferences.
     */
    suspend fun getKeyPair() : Flow<Resource<KeyPair?>>
}