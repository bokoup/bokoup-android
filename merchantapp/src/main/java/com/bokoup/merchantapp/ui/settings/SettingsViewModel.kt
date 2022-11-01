package com.bokoup.merchantapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.domain.SettingsRepo
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {
    private val dispatcher = Dispatchers.IO

    val addressConsumer = ResourceFlowConsumer<String>(viewModelScope)
    val mnemonicConsumer = ResourceFlowConsumer<List<String>>(viewModelScope)

    fun saveMnemonic(mnemonic: List<String>) = viewModelScope.launch(dispatcher) {
        addressConsumer.collectFlow(
            settingsRepo.saveMnemonic(mnemonic)
        )
    }

    fun generateMnemonic(phraseLength: MnemonicPhraseLength) = viewModelScope.launch(dispatcher) {
        mnemonicConsumer.collectFlow(
            settingsRepo.generateMnemonic(phraseLength)
        )
    }

    fun getMnemonic() = viewModelScope.launch(dispatcher) {
        mnemonicConsumer.collectFlow(
            settingsRepo.getMnemonic()
        )
    }
}