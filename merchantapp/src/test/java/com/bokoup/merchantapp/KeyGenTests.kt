package com.bokoup.merchantapp

import com.dgsd.ksol.core.model.PrivateKey
import com.dgsd.ksol.keygen.KeyFactory
import kotlinx.coroutines.runBlocking
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
}