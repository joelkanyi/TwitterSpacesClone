package com.kanyideveloper.twitterspacesclone.screens.space

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.twitterspacesclone.R
import com.kanyideveloper.twitterspacesclone.ui.theme.TwitterBlue
import com.ramcosta.composedestinations.annotation.Destination
import live.hms.video.connection.degredation.Peer
import live.hms.video.media.tracks.HMSAudioTrack
import live.hms.video.sdk.models.HMSPeer

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SpaceScreen(
    /* peers: State<List<HMSPeer>>*/
) {

    Box(Modifier.fillMaxSize()) {
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
                    items(/*peers.value.size*/10) {
                        PeerItem(
                            //peers.value[it]
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
                .background(Color.White)
        )
    }
}

@Composable
fun BottomMicItem(
    modifier: Modifier = Modifier
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
                    onClick = { /*TODO*/ },
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
fun PeerItem(/*peer: HMSPeer*/) {
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
            /*peer.name*/"Joel Kanyi",
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
                painter = painterResource(id = R.drawable.ic_mute_mic),
                modifier = Modifier
                    .size(12.dp),
                tint = Color.Red,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                /*text = peer.hmsRole.name*/"Speaker",
                textAlign = TextAlign.Right,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}