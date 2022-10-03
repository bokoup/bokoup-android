package com.bokoup.merchantapp.ui.tender

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.bokoup.merchantapp.BuildConfig

@Composable
@ExperimentalMaterial3Api
fun CreateTenderCard(
    setCardState: (Boolean) -> Unit,
    createTender: (
        label: String,
        labelKey: String,
        enabled: Boolean,
        opensCashDrawer: Boolean
    ) -> Unit
) {
    var label by remember {
        mutableStateOf(TextFieldValue())
    }
    var labelKey by remember {
        mutableStateOf(TextFieldValue(text = BuildConfig.APPLICATION_ID))
    }
    var enabled by remember {
        mutableStateOf(true)
    }

    var opensCashDrawer by remember {
        mutableStateOf(false)
    }

    Card {
        Column(
            Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .width(400.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Create Tender",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                IconButton(
                    onClick = { setCardState(false) },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        "Close",

                    )
                }
            }
            TextField(
                value = label,
                onValueChange = { text -> label = text },
                label = { Text("Label") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            TextField(
                value = labelKey,
                onValueChange = { text -> labelKey = text },
                label = { Text("Label Key") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enabled")
                Switch(checked = enabled, onCheckedChange = { enabled = it })
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Opens Cash Drawer")
                Switch(checked = opensCashDrawer, onCheckedChange = { enabled = it })
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    createTender(label.text, labelKey.text, enabled, opensCashDrawer)
                    setCardState(false)
                }) {
                    Text("Create Tender")
                }
            }

        }

    }
}