package com.bokoup.merchantapp.domain

import android.content.SharedPreferences
import com.bokoup.lib.resourceFlowOf
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength

class SettingsRepoImpl(
    private val keyFactory: KeyFactory,
    private val sharedPref: SharedPreferences
) : SettingsRepo {
    override suspend fun saveMnemonic(mnemonic: List<String>) = resourceFlowOf {
        val keypair = keyFactory.createKeyPairFromMnemonic(mnemonic)
        with(sharedPref.edit()) {
            putStringSet("deviceKeyPair", mnemonic.toSet())
            commit()
        }
        keypair.publicKey.toString()
    }

    override suspend fun generateMnemonic(phraseLength: MnemonicPhraseLength) =
        resourceFlowOf { keyFactory.createMnemonic(phraseLength)  }

    override suspend fun getKeyPair() =
        resourceFlowOf {
            val mnemonic = sharedPref.getStringSet("deviceKeyPair", emptySet())
            if (mnemonic != null) {
                keyFactory.createKeyPairFromMnemonic(mnemonic.toList())
            } else {
                null
            }
        }

    override suspend fun getMnemonic() = resourceFlowOf {
        sharedPref.getStringSet("deviceKeyPair", emptySet())?.toList() ?: emptyList()
    }
}