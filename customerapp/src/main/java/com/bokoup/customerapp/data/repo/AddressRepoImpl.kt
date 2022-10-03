package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.AddressDao
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.SolanaRepo
import com.bokoup.lib.resourceFlowOf
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength

class AddressRepoImpl(
    private val addressDao: AddressDao,
    private val solanaRepo: SolanaRepo
) : AddressRepo {

    override suspend fun insertAddressPrep(active: Boolean?) {
        val words = KeyFactory.createMnemonic(MnemonicPhraseLength.TWELVE)
        val newKeyPair = KeyFactory.createKeyPairFromMnemonic(words)

        val address = Address(
            id = newKeyPair.publicKey.toString(),
            phrase = words.joinToString(),
            active = active
        )
        addressDao.insertAddress(address)
        solanaRepo.airDrop(newKeyPair.publicKey)
    }

    override fun insertAddress(active: Boolean?) = resourceFlowOf {
        insertAddressPrep(active)
    }

    override fun getAddresses() = resourceFlowOf {
        var addresses = addressDao.getAddresses()
        if (addresses.isEmpty()) {
            insertAddressPrep(true)
            addresses = addressDao.getAddresses()
        }
        addresses
    }

    override fun getAddress(id: String) = resourceFlowOf { addressDao.getAddress(id) }
    override fun getActiveAddress() = resourceFlowOf { addressDao.getActiveAddress() }
    override fun updateActive(id: String) {
        addressDao.clearActive()
        addressDao.setActive(id)
    }
}