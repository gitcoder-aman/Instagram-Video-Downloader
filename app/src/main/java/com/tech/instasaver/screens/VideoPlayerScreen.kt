package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun VideoPlayerScreen(videoUri: String?, navController: NavHostController) {

    Log.d("file@@", "VideoPlayerScreen: ${videoUri}")
    val context = LocalContext.current
    //when user run app in background then do task
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val player = remember { SimpleExoPlayer.Builder(context).build() }
    val playerView = remember { PlayerView(context) }

    DisposableEffect(Unit) {
        playerView.player = player
        player.setMediaItem(videoUri?.toUri()?.let { MediaItem.fromUri(it) }!!)
        player.prepare()

        onDispose {
            player.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        AndroidView(
            factory = {
                playerView
            }
            , update = { //when user run app in background then do task
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    else -> Unit
                }
            }
            , modifier = Modifier
                .fillMaxSize()
//                .aspectRatio(16 / 9f)
        )
    }
    BackHandler(enabled = true) {
        navController.navigateUp()
    }
}

