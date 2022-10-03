package com.bokoup.merchantapp.ui.tender

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun TenderScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    channel: Channel<String>,
) {
    var (cardState, setCardState) =  remember {
        mutableStateOf(false)
    }

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Tender,
        content = {
            TenderContent(padding = it, channel = channel, cardState = cardState, setCardState = setCardState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                setCardState(true)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "create tender")
            }
        },
    )
}

