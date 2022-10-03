package com.bokoup.customerapp.ui.tokens


import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.R
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen

@Composable
@ExperimentalMaterial3Api
fun TokensScreen(
    viewModel: TokensViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getTokens()
    }

    val actions: @Composable (RowScope.() -> Unit) = {
        IconButton(onClick = { /* TODO: Open search */ }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.app_name)
            )
        }
    }
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Tokens,
        topBarActions = actions,
        content = { TokensContent(padding = it, tokens = viewModel.tokens) }
    )

}