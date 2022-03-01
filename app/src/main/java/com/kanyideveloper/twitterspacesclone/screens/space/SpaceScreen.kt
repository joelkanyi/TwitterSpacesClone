package com.kanyideveloper.twitterspacesclone.screens.space

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.twitterspacesclone.R
import com.ramcosta.composedestinations.annotation.Destination
import live.hms.video.connection.degredation.Peer
import live.hms.video.media.tracks.HMSAudioTrack
import live.hms.video.sdk.models.HMSPeer

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SpaceScreen(
    peers: State<List<HMSPeer>>
) {
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
            items(peers.value.size) {
                PeerItem(
                    peers.value[it]
                )
            }
        }
    }
}

@Composable
fun PeerItem(peer: HMSPeer) {
    Column(
        Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            peer.name,
            modifier = Modifier
                .background(Color(0x80CCCCCC))
                .padding(4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mute_mic),
                modifier = Modifier
                    .size(12.dp),
                tint = Color.Red,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = peer.hmsRole.name,
                textAlign = TextAlign.Right,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}