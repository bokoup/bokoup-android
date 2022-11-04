package com.bokoup.merchantapp.domain

import android.content.SharedPreferences
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.model.SharedPrefKeys
import com.bokoup.merchantapp.model.key
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
        val publicKeyString = keyPair.publicKey.toBase58String()

        with(sharedPref.edit()) {
            putString(SharedPrefKeys.KeyPairString.key, keyPairString)
            putString(SharedPrefKeys.PublicKeyString.key, publicKeyString)
            commit()
        }
        keyPair.publicKey.toBase58String()
    }

    override suspend fun saveGroupSeed(groupSeed: String) = resourceFlowOf {
        with(sharedPref.edit()) {
            putString(SharedPrefKeys.GroupSeed.key, groupSeed)
            commit()
        }
        groupSeed
    }

    override suspend fun generateMnemonic(phraseLength: MnemonicPhraseLength) =
        resourceFlowOf { keyFactory.createMnemonic(phraseLength)  }

    override suspend fun getKeyPairfromString(bytesString: String) : KeyPair {
        val bytesList = bytesString.split(",").map {
            it.toInt()
        }
        if (bytesList.size != 64 && bytesList.any{ it < 0 || it > 255 }) {
            throw Exception("Invalid bytesString")
        }

        val privateKey = PrivateKey.fromByteArray(bytesList.map{ it.toByte()}.toByteArray())

        return KeyFactory.createKeyPairFromPrivateKey(privateKey)
    }

    override suspend fun getPublicKeyString() : String? {
        return sharedPref.getString(SharedPrefKeys.PublicKeyString.key, "")
    }

    override suspend fun getKeyPairString() : String? {
        return sharedPref.getString(SharedPrefKeys.KeyPairString.key, "")
    }

    override suspend fun getKeyPair() : KeyPair? {
        val keyPairString = getKeyPairString()
        return if (keyPairString == null) {
            null
        } else {
            getKeyPairfromString(keyPairString)
        }
    }

    override suspend fun getKeyPairFlow() =
        resourceFlowOf {
            getKeyPair()
        }

    override suspend fun getGroupSeed() = resourceFlowOf {
        sharedPref.getString(SharedPrefKeys.GroupSeed.key, "") ?: ""
    }
}