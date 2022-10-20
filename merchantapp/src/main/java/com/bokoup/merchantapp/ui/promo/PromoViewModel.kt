package com.bokoup.merchantapp.ui.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.data.DataRepo
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.CreatePromoResult
import com.bokoup.merchantapp.model.PromoWithMetadataJson
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

    val promoSubscription = dataRepo.promoSubscriptionFlow

    val promosConsumer = ResourceFlowConsumer<List<PromoWithMetadataJson>>(viewModelScope)
    val appIdConsumer = ResourceFlowConsumer<AppId>(viewModelScope)
    val createPromoConsumer = ResourceFlowConsumer<CreatePromoResult>(viewModelScope)

    val errorConsumer = merge(
        promosConsumer.error,
        appIdConsumer.error,
        createPromoConsumer.error,
    )

    val isLoadingConsumer = merge(
        promosConsumer.isLoading,
    )

    fun fetchPromos() = viewModelScope.launch(dispatcher) {
        promosConsumer.collectFlow(
            dataRepo.fetchPromos()
        )
    }
    fun createPromo(createPromoArgs: CreatePromoArgs) = viewModelScope.launch(dispatcher) {
        createPromoConsumer.collectFlow(
            dataRepo.createPromo(createPromoArgs)
        )
    }

    fun fetchAppId() = viewModelScope.launch(dispatcher) {
        appIdConsumer.collectFlow(
            dataRepo.fetchAppId()
        )
    }

    init {
        fetchPromos()
    }

}