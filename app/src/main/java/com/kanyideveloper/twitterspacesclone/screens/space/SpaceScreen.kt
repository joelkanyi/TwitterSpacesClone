package com.kanyideveloper.twitterspacesclone.screens.space

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.twitterspacesclone.R
import com.ramcosta.composedestinations.annotation.Destination
import live.hms.video.connection.degredation.Peer

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SpaceScreen() {
    Column(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Right,
            text = "Leave",
            color = Color.Red,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "Building a Twitter Spaces Clone with 100ms SDK and Jetpack Compose",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(30) {
                PeerItem(
                    name = "Joel Kanyi",
                    role = "Host"
                )
            }
        }

    }
}

@Composable
fun PeerItem(name: String, role: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentDescription = null
        )
        Text(
            text = "Joel Kanyi",
            textAlign = TextAlign.Right,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Speaker",
            textAlign = TextAlign.Right,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}