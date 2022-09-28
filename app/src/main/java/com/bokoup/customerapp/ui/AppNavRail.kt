package com.bokoup.customerapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bokoup.customerapp.R
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.theme.AppTheme


@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Icon(
                painterResource(R.drawable.ic_bokoup_logo),
                null,
                Modifier.padding(vertical = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute == Screen.Tokens.name,
            onClick = navigateToHome,
            icon = { Icon(Icons.Filled.Home, stringResource(Screen.Tokens.title)) },
            label = { Text(stringResource(Screen.Tokens.title)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == Screen.Wallet.name,
            onClick = navigateToSettings,
            icon = { Icon(Icons.Filled.Settings, stringResource(Screen.Wallet.title)) },
            label = { Text(stringResource(Screen.Wallet.title)) },
            alwaysShowLabel = false
        )
        Spacer(Modifier.weight(1f))
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    AppTheme {
        AppNavRail(
            currentRoute = Screen.Tokens.name,
            navigateToHome = {},
            navigateToSettings = {},
        )
    }
}
