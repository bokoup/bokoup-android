package com.bokoup.merchantapp.domain

import android.content.SharedPreferences
import com.bokoup.lib.resourceFlowOf
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.core.model.PrivateKey
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength

class SettingsRepoImpl(
    private val keyFactory: KeyFactory,
    private val sharedPref: SharedPreferences
) : SettingsRepo {
    override suspend fun saveKeyPairString(keyPairString: String) = resourceFlowOf {
        val bytesList = keyPairString.split(",").map {
            it.toInt().toByte()
        }

        val privateKey = PrivateKey.fromByteArray(bytesList.toByteArray())

        val keyPair = KeyFactory.createKeyPairFromPrivateKey(privateKey)

        with(sharedPref.edit()) {
            putString("deviceKeyPair", keyPairString)
            commit()
        }
        keyPair.publicKey.toBase58String()
    }

    override suspend fun saveGroupSeed(groupSeed: String) = resourceFlowOf {
        with(sharedPref.edit()) {
            putString("groupSeed", groupSeed)
            commit()
        }
        groupSeed
    }

    override suspend fun generateMnemonic(phraseLength: MnemonicPhraseLength) =
        resourceFlowOf { keyFactory.createMnemonic(phraseLength)  }

    override suspend fun getKeyPairfromBytesString(bytesString: String) : KeyPair {
        val bytesList = bytesString.split(",").map {
            it.toInt()
        }
        if (bytesList.size != 64 && bytesList.any{ it < 0 || it > 255 }) {
            throw Exception("Invalid bytesString")
        }

        val privateKey = PrivateKey.fromByteArray(bytesList.map{ it.toByte()}.toByteArray())

        return KeyFactory.createKeyPairFromPrivateKey(privateKey)
    }


    override suspend fun getKeyPairFlow() =
        resourceFlowOf {
            val bytesString = sharedPref.getString("deviceKeyPair", "")
            if (bytesString != null) {
                getKeyPairfromBytesString(bytesString)
            } else {
                null
            }
        }

    override suspend fun getGroupSeed() = resourceFlowOf {
        sharedPref.getString("groupSeed", "") ?: ""
    }
}