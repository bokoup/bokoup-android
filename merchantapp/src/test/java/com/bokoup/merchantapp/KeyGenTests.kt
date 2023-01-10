package com.bokoup.merchantapp

import android.util.Log
import com.dgsd.ksol.core.model.PrivateKey
import com.dgsd.ksol.keygen.KeyFactory
import kotlinx.coroutines.runBlocking
import org.bitcoinj.crypto.HDUtils
import org.junit.Assert
import org.junit.Test

class KeyGenTests {
    @Test
    fun serialize_and_deserialize_bytes_string() {
        val bytesStringIn = "1,2,3,4,5"
        val bytesList = bytesStringIn.split(",").map {
            it.toInt().toUByte()
        }
        val bytesStringOut = bytesList.joinToString(",") { it.toInt().toString() }

        assert(bytesStringIn == bytesStringOut)
    }

    @Test
    fun creates_keyPair_from_bytes_string() {
        runBlocking {
            val keyPairString = "228,90,68,53,159,204,206,179,98,60,154,90,1,41,147,174,232,141,118,157,163,96,83,58,24,230,62,111,57,189,59,3,158,47,76,184,25,251,118,192,254,244,167,50,34,239,168,131,125,6,118,129,70,15,37,69,78,110,238,160,128,96,5,50"

            val bytesList = keyPairString.split(",").map {
                it.toInt().toByte()
            }

            val privateKey = PrivateKey.fromByteArray(bytesList.toByteArray())

            val keyPair = KeyFactory.createKeyPairFromPrivateKey(privateKey)

            assert(keyPair.publicKey.toBase58String() == "BeVFQNXVFe67zpB4ANq17NxiACgV2sauLRXmaVoj3EPs")
        }
    }

    @Test
    fun creates_seed_from_seedPhrase_and_passphrase() {
        runBlocking {
            val seedPhrase = "sentence ugly section antenna motion bind adapt vault increase milk lawn humor"
            val passphrase = "password"
            val seedString = "254, 23, 3, 86, 117, 10, 116, 169, 39, 72, 179, 99, 38, 93, 96, 239, 217, 1, 52, 178, 21, 22, 164, 231, 92, 25, 157, 86, 15, 57, 72, 212, 42, 22, 209, 171, 20, 122, 63, 54, 233, 159, 151, 229, 63, 235, 4, 139, 222, 198, 255, 153, 92, 209, 52, 38, 26, 133, 7, 221, 221, 127, 194, 249"

            val bytesList = seedString.split(", ").map {
                it.toInt().toByte()
            }.toByteArray()



            val seed = KeyFactory.createSeedFromMnemonic(seedPhrase.split(" "), passphrase)

            Assert.assertArrayEquals(bytesList, seed)

            val SOLANA_CURVE_SEED = "ed25519 seed".toByteArray()
            val sha512 = HDUtils.hmacSha512(SOLANA_CURVE_SEED, seed)

            Log.d("KeyGenTests", "$sha512")
            Assert.assertArrayEquals(sha512, seed)



//            assert(bytesList == seed) {
//                "byteList $bytesList != seed $seed"
//            }






//            val privateKey = PrivateKey.fromByteArray(bytesList.toByteArray())
//
//            val keyPair = KeyFactory.createKeyPairFromPrivateKey(privateKey)
//
//            assert(keyPair.publicKey.toBase58String() == "BAsnycHCGXVpS9yG76HsBb4v92sot5VuYan8urAgg4hP")
        }
    }
}