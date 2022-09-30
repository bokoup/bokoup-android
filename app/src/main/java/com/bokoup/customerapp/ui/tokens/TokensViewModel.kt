package com.bokoup.customerapp.ui.tokens

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.dom.model.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokensViewModel @Inject constructor(private val repo: TokenRepo) : ViewModel() {
    var tokens by mutableStateOf(emptyList<Token>())
    val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getTokens() = withContext(dispatcher) {
        repo.getTokensFromRoom().collect { dbTokens ->
            tokens = dbTokens
        }
    }

}