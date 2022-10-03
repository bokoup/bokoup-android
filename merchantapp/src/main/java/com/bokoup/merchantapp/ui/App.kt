package com.bokoup.merchantapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bokoup.merchantapp.nav.NavGraph
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.theme.AppTheme

@Composable
@ExperimentalMaterial3Api
fun App(startDestination: String = Screen.Tender.route) {
    AppTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController, startDestination)
    }
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
fun DefaultPreview() {
    AppTheme(isDarkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            App()
        }
    }
}