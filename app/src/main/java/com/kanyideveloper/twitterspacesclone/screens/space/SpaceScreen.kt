package com.kanyideveloper.twitterspacesclone.screens.space

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.twitterspacesclone.R
import com.kanyideveloper.twitterspacesclone.ui.theme.TwitterBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import live.hms.video.sdk.models.HMSPeer

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SpaceScreen(
    name: String,
    navigator: DestinationsNavigator,
    viewModel: SpaceViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.startMeeting(name)
    }

    Box(Modifier.fillMaxSize()) {

        val peers = viewModel.peers.value


        if (viewModel.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .padding(12.dp)
            )
        }

        Box(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            viewModel.leaveTheSpace()
                            navigator.popBackStack()
                        },
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
                    items(peers) { peer ->
                        PeerItem(peer, viewModel)
                    }
                }
            }
        }

        BottomMicItem(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(Color.White),
            viewModel = viewModel
        )
    }
}

@Composable
fun BottomMicItem(
    modifier: Modifier = Modifier,
    viewModel: SpaceViewModel
) {

    val hmsLocalPeer = viewModel.localPeer.value

    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(Modifier.fillMaxWidth(0.2f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        if (hmsLocalPeer?.hmsRole?.name == "listener") {
                            return@IconButton
                        }

                        viewModel.setLocalAudioEnabled(
                            !viewModel.isLocalAudioEnabled()
                        )
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color.LightGray, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_big_mic),
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }

                Text(
                    text = when (hmsLocalPeer?.audioTrack?.isMute) {
                        true -> {
                            "Mic is off"
                        }
                        false -> {
                            "Mic is on"
                        }
                        else -> {
                            "Null"
                        }
                    },
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
        }


        Row(
            Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.Filled.PeopleOutline,
                contentDescription = null
            )

            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null
            )

            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = null
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(TwitterBlue)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_feather),
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun PeerItem(peer: HMSPeer, viewModel: SpaceViewModel) {

    val colors = listOf(
        0xFF556b2f,
        0xFF5f6f7e,
        0xFF8c53c6,
        0xFFcc0000,
        0xFF8b4513,
    )

    Column(
        Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

            Box(
                modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(colors.random())),
                contentAlignment = Alignment.Center
            ){

                Text(
                    text = viewModel.getNameInitials(peer.name),
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
        }

        Text(
            peer.name,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = if (peer.audioTrack?.isMute == true) {
                    painterResource(id = R.drawable.ic_mute_mic)
                } else {
                    painterResource(id = R.drawable.audio_wave)
                },
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
                fontWeight = FontWeight.Light
            )
        }
    }
}