package com.bokoup.merchantapp.ui.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.PromoListQuery
import com.bokoup.merchantapp.data.DataRepo
import com.bokoup.merchantapp.model.AppId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoViewModel @Inject constructor(
    private val dataRepo: DataRepo,
) : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    val promosConsumer = ResourceFlowConsumer<List<PromoListQuery.Promo>>(viewModelScope)
    val appIdConsumer = ResourceFlowConsumer<AppId>(viewModelScope)

    val errorConsumer = merge(
        promosConsumer.error,
        appIdConsumer.error,
    )

    val isLoadingConsumer = merge(
        promosConsumer.isLoading,
        appIdConsumer.isLoading,
    )

    fun fetchPromos() = viewModelScope.launch(dispatcher) {
        promosConsumer.collectFlow(
            dataRepo.fetchPromos()
        )
    }

    fun fetchAppId() = viewModelScope.launch(dispatcher) {
        appIdConsumer.collectFlow(
            dataRepo.fetchAppId()
        )
    }

    init {
        fetchAppId()
    }

}