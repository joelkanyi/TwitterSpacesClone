package com.kanyideveloper.twitterspacesclone.screens.space

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import com.kanyideveloper.twitterspacesclone.R
import com.kanyideveloper.twitterspacesclone.ui.theme.TwitterBlue
import com.kanyideveloper.twitterspacesclone.util.Resource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import live.hms.video.connection.degredation.Peer
import live.hms.video.media.tracks.HMSAudioTrack
import live.hms.video.sdk.models.HMSPeer
import timber.log.Timber

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

    Timber.d("All peers: ${viewModel.peers.value}")

    Box(Modifier.fillMaxSize()) {

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

                    val peers = viewModel.peers.value
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(4),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(peers) { peer ->
                            Timber.d(peer.toString())
                            PeerItem(
                                peer
                            )
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
                        viewModel.setLocalAudioEnabled(
                            !viewModel.isLocalAudioEnabled()!!
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
                    text = "Mic is on",
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
                painter = if (peer.audioTrack?.isMute!!) {
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