package com.bokoup.customerapp.ui.tokens.components

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.dom.model.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokensViewModel @Inject constructor(private val repo: TokenRepo) : ViewModel() {
    var tokens by mutableStateOf(emptyList<Token>())

    fun getTokens() = viewModelScope.launch {
        repo.getTokensFromRoom().collect { dbTokens ->
            tokens = dbTokens
        }
    }

}