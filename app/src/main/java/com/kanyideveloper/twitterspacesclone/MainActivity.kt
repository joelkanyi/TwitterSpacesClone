package com.kanyideveloper.twitterspacesclone

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.kanyideveloper.twitterspacesclone.screens.NavGraphs
import com.kanyideveloper.twitterspacesclone.ui.theme.Shapes
import com.kanyideveloper.twitterspacesclone.ui.theme.TwitterSpacesCloneTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwitterSpacesCloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val microphonePermission = rememberPermissionState(
                        Manifest.permission.RECORD_AUDIO
                    )

                    if (microphonePermission.permission == Manifest.permission.RECORD_AUDIO) {
                        when {
                            microphonePermission.hasPermission -> {
                                Timber.d("Audio Record permission granted")
                            }
                            microphonePermission.shouldShowRationale -> {
                                Timber.d("Audio Record permission is needed to talk in a space")
                            }
                            !microphonePermission.hasPermission && !microphonePermission.shouldShowRationale -> {
                                Timber.d("Audio Record permission denied permanently")
                            }
                        }
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(
                        key1 = lifecycleOwner,
                        effect = {
                            val observer = LifecycleEventObserver { _, event ->
                                if (event == Lifecycle.Event.ON_RESUME) {
                                    microphonePermission.launchPermissionRequest()
                                }
                            }

                            lifecycleOwner.lifecycle.addObserver(observer)

                            onDispose {
                                lifecycleOwner.lifecycle.removeObserver(observer)
                            }
                        })


                    val navController = rememberNavController()
                    val navHostEngine = rememberNavHostEngine()
                    val newBackStackEntry by navController.currentBackStackEntryAsState()
                    val route = newBackStackEntry?.destination?.route

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text("Spaces")
                                },
                                backgroundColor = MaterialTheme.colors.surface,
                                elevation = 5.dp
                            )
                        }
                    ) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            engine = navHostEngine
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TwitterSpacesCloneTheme {
    }
}