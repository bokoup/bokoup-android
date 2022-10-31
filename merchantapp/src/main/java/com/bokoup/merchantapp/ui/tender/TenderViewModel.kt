package com.bokoup.merchantapp.ui.tender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.merchantapp.data.TenderRow
import com.bokoup.merchantapp.domain.TenderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenderViewModel @Inject constructor(
    private val tenderRepo: TenderRepository,
) : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    val tendersConsumer = ResourceFlowConsumer<List<TenderRow>>(viewModelScope)
    private val tendersServiceConsumer = ResourceFlowConsumer<Unit>(viewModelScope)

    val errorConsumer = merge(
        tendersConsumer.error,
        tendersServiceConsumer.error,
    )

    val isLoadingConsumer = merge(
        tendersConsumer.isLoading,
//        tendersServiceConsumer.isLoading,
    )

    fun getTenders() = viewModelScope.launch(dispatcher) {
        tendersConsumer.collectFlow(
            tenderRepo.getTenders()
        )
    }

    fun createTender(
        label: String,
        labelKey: String,
        enabled: Boolean = true,
        opensCashDrawer: Boolean = false
    ) = viewModelScope.launch(dispatcher) {
        tendersServiceConsumer.collectFlow(
            tenderRepo.createTender(
                label,
                labelKey,
                enabled,
                opensCashDrawer
            )
        )
        getTenders()
    }

    fun deleteTender(
        tenderId: String,
    ) = viewModelScope.launch(dispatcher) {
        tendersServiceConsumer.collectFlow(tenderRepo.deleteTender(tenderId))
        getTenders()
    }

    fun setTenderEnabled(
        tenderId: String,
        enabled: Boolean
    ) = viewModelScope.launch(dispatcher) {
        tendersServiceConsumer.collectFlow(tenderRepo.setTenderEnabled(tenderId, enabled))
        getTenders()
    }

    fun connect() = viewModelScope.launch(dispatcher) {
        tenderRepo.connectTenders()
    }
    fun disconnect() = viewModelScope.launch(dispatcher) {
        tenderRepo.disconnectTenders()
    }

}