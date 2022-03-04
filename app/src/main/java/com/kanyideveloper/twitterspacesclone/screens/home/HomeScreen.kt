package com.kanyideveloper.twitterspacesclone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.twitterspacesclone.R
import com.kanyideveloper.twitterspacesclone.screens.destinations.SpaceScreenDestination
import com.kanyideveloper.twitterspacesclone.screens.space.SpaceViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: SpaceViewModel = hiltViewModel()
) {

    Timber.d(viewModel.peers.value.toString())

    LazyColumn {

        item {
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = "Happening Now",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Spaces going on right now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        val colors = listOf(
            0xFF556b2f,
            0xFF5f6f7e,
            0xFF8c53c6,
            0xFFcc0000,
            0xFF8b4513,
        )

        items(10) {
            val randomColor = (Color(colors.random()))
            SpaceItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(randomColor),
                viewModel = viewModel,
                navigator = navigator
            )
        }
    }
}

@Composable
fun SpaceItem(
    modifier: Modifier = Modifier,
    viewModel: SpaceViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp)
            .clickable {
                navigator.navigate(SpaceScreenDestination())
            },
        elevation = 5.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.audio_wave),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "LIVE",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                Icon(
                    imageVector = Icons.Default.MoreVert,
                    tint = Color.White,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Building a Twitter Spaces Clone with 100ms SDK and Jetpack Compose",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "1 Listening",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Joel Kanyi",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Host",
                    style = typography.body1.merge(),
                    color = Color.White,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                size = 3.dp,
                            ),
                        )
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 2.dp,
                            bottom = 2.dp
                        )
                )
            }
        }
    }
}