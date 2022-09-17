package com.bokoup.customerapp.ui.tokens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bokoup.customerapp.dom.model.Token

@Composable
@ExperimentalMaterial3Api
fun TokensContent(
    padding: PaddingValues,
    tokens: List<Token>
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
        items(items = tokens) {
            token -> Text(text = token.id)
        }
    }
}