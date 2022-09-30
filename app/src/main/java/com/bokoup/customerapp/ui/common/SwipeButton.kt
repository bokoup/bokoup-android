package com.bokoup.customerapp.ui.common

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bokoup.customerapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun SwipeIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .padding(6.dp)
            .aspectRatio(
                ratio = 1F,
                matchHeightConstraintsFirst = true,
            )
            .background(backgroundColor)
            .border(ButtonDefaults.outlinedButtonBorder, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier,
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun SwipeButton(
    text: String,
    isComplete: Boolean = false,
    isSuccess: Boolean = true,
    doneImageVector: ImageVector = Icons.Rounded.Done,
    errorImageVector: ImageVector = Icons.Rounded.Error,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onSwipe: () -> Unit,
    swipeComplete: Boolean,
    setSwipeComplete: (Boolean) -> Unit,
) {
    val width = 200.dp
    val widthInPx = with(LocalDensity.current) {
        width.toPx()
    }
    val anchors = mapOf(
        0F to 0,
        widthInPx to 1,
    )

    val swipeableState = rememberSwipeableState(
        initialValue = if (swipeComplete) {
            1
        } else {
            0
        }
    )

    val alpha: Float by animateFloatAsState(
        targetValue = if (swipeComplete) {
            0F
        } else {
            1F
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        )
    )

    LaunchedEffect(
        key1 = swipeableState.currentValue,
    ) {
        if (swipeableState.currentValue == 1 && !swipeComplete) {
            setSwipeComplete(true)
            onSwipe()
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(
                horizontal = 64.dp,
            )
            .clip(CircleShape)
            .animateContentSize()
            .then(
                if (swipeComplete) {
                    Modifier
                        .width(64.dp)
                        .background(Color.Transparent)
                } else {
                    Modifier
                        .width(250.dp)
                        .background(backgroundColor)
                }
            ),
    ) {
        if (!isComplete) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(1 - swipeableState.offset.value / widthInPx)
            )
        }
        SwipeIndicator(
            modifier = modifier
                .align(Alignment.CenterStart)
                .alpha(alpha)
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.3F)
                    },
                    orientation = Orientation.Horizontal,
                ),
            backgroundColor = backgroundColor,
        )
        AnimatedVisibility(
            visible = swipeComplete && !isComplete,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = ButtonDefaults.outlinedButtonBorder.width,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            )
        }
        AnimatedVisibility(
            visible = swipeComplete && isComplete,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                imageVector = if (isSuccess) {
                    doneImageVector
                } else {
                    errorImageVector
                },
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

    }

}


@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme(isDarkTheme = true) {
        Surface(
            modifier = Modifier
                .height(64.dp)
        ) {
            SwipeButton(
                text = "jingus",
                onSwipe = {},
                swipeComplete = false,
                setSwipeComplete = {}
            )
        }
    }
}