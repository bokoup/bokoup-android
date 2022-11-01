package com.bokoup.merchantapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bokoup.merchantapp.R
import com.bokoup.merchantapp.model.Screen
import com.bokoup.merchantapp.ui.theme.AppTheme

@Composable
@ExperimentalMaterial3Api
fun AppTopBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
    screen: Screen,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = screen.title, style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(R.drawable.ic_bokoup_logo),
                    contentDescription = stringResource(R.string.app_name),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview("Home list drawer screen")
@Preview("Home list drawer screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Home list drawer screen (big font)", fontScale = 1.5f)
@Composable
@ExperimentalMaterial3Api
fun PreviewHomeListDrawerScreen() {
    AppTheme {
        AppTopBar(openDrawer = { /*TODO*/ }, screen = Screen.Customer)
    }
}