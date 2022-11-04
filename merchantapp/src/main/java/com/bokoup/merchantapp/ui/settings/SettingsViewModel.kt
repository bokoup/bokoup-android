package com.bokoup.merchantapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.domain.SettingsRepo
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
    val groupSeedConsumer = ResourceFlowConsumer<String>(viewModelScope)

    fun saveKeyPairString(keyPairString: String) = viewModelScope.launch(dispatcher) {
        addressConsumer.collectFlow(
            settingsRepo.saveKeyPairString(keyPairString)
        )
    }

    fun saveGroupSeed(groupSeed: String) = viewModelScope.launch(dispatcher) {
        groupSeedConsumer.collectFlow(
            settingsRepo.saveGroupSeed(groupSeed)
        )
    }

    fun getGroupSeed() = viewModelScope.launch(dispatcher) {
        groupSeedConsumer.collectFlow(
            settingsRepo.getGroupSeed()
        )
    }
}