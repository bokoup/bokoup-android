package com.bokoup.customerapp.ui.tokens


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.ui.tokens.components.TokensContent
import com.bokoup.customerapp.ui.tokens.components.TokensTopBar
import com.bokoup.customerapp.ui.tokens.components.TokensViewModel

@Composable
@ExperimentalMaterial3Api
fun TokensScreen(
    viewModel: TokensViewModel = hiltViewModel(),
    openDrawer: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getTokens()
    }
    Scaffold(
        topBar = {
            TokensTopBar(openDrawer = openDrawer)
        },
        content = { padding ->
            TokensContent(padding = padding, tokens = viewModel.tokens)
        }
    )
}