package com.bokoup.customerapp.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bokoup.customerapp.R
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navController: NavController,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {
        BokoupLogo(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(Screen.Tokens.title)) },
            icon = { Icon(Icons.Filled.Loyalty, null) },
            selected = currentRoute == Screen.Tokens.name,
            onClick = { navController.navigate(Screen.Tokens.name); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = Screen.Wallet.title)) },
            icon = { Icon(Icons.Filled.Wallet, null) },
            selected = currentRoute == Screen.Wallet.name,
            onClick = { navController.navigate(Screen.Wallet.name); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = Screen.Share.title)) },
            icon = { Icon(Icons.Filled.Share, null) },
            selected = currentRoute == Screen.Share.name,
            onClick = { navController.navigate(Screen.Share.name); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = Screen.Scan.title)) },
            icon = { Icon(Icons.Filled.QrCodeScanner, null) },
            selected = currentRoute == Screen.Scan.name,
            onClick = { navController.navigate(Screen.Scan.name); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = Screen.Approve.title)) },
            icon = { Icon(Icons.Filled.Approval, null) },
            selected = currentRoute == Screen.Approve.name,
            onClick = { navController.navigate(Screen.Approve.name); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun BokoupLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painterResource(R.drawable.ic_bokoup_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleLarge)


    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    AppTheme {
        AppDrawer(
            currentRoute = Screen.Tokens.name,
            navController = rememberNavController(),
            closeDrawer = { }
        )
    }
}
