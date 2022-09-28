package com.bokoup.customerapp.ui.approve


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bokoup.customerapp.R
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.ui.common.SwipeButton
import com.dgsd.ksol.model.LocalTransaction

@Composable
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun ApproveContent(
    padding: PaddingValues,
    appId: TokenApiId?,
    tokenApiResponse: TokenApiResponse?,
    localTransaction: LocalTransaction?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (appId != null) {
            Text(text = appId.label, modifier = Modifier.padding(vertical = 16.dp))
            AsyncImage(
                model = appId.icon,
                placeholder = painterResource(id = R.drawable.ic_bokoup_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
        if (tokenApiResponse != null) {
            Text(text = tokenApiResponse.message, modifier = Modifier.padding(vertical = 16.dp))
            Row(modifier = Modifier.height(64.dp)) {
                SwipeButton(
                    text = "Approve",
                    onSwipe = { Log.d("jingus", localTransaction.toString()) })
            }

        }
    }
}