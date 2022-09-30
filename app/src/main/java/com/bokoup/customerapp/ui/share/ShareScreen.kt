package com.bokoup.customerapp.ui.share


import android.graphics.Bitmap
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun ShareScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Share,
        content = {
            ShareContent(padding = it)
        }
    )

}