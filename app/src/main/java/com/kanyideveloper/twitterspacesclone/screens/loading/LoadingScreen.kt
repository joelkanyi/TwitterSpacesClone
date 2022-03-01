package com.kanyideveloper.twitterspacesclone.screens.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun LoadingScreen() {
    val pad = 8.dp

    Box(
        modifier = Modifier.fillMaxSize().padding(pad),
        contentAlignment = Alignment.Center
    ) {

        val circleSize = min(
            LocalConfiguration.current.screenWidthDp,
            LocalConfiguration.current.screenHeightDp
        ).dp - pad * 2

        CircularProgressIndicator(
            strokeWidth = 14.dp,
            modifier = Modifier
                .size(circleSize)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoadingScreen()
}