package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import com.tech.instasaver.ui.theme.InstaSaverTheme
import com.tech.instasaver.ui.theme.PinkColor

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnsafeOptInUsageError")
class VideoPlayerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var videoUri: String? = null

        if (intent != null) {
            if (intent.getStringExtra("history_video_uri") != null) {
                videoUri = intent.getStringExtra("history_video_uri")
            }else if(intent.getStringExtra("home_video_url") != null){
                videoUri = intent.getStringExtra("home_video_url")
            }
        }
        setContent {
            InstaSaverTheme {
                Log.d("file@@", "VideoPlayerScreen: $videoUri")

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Video",
                                color = Color.Black,
                                fontSize = 20.sp,
                            )

                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onBackPressed()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = PinkColor,
                            navigationIconContentColor = Color.Black,
                            titleContentColor = Color.Gray
                        )
                    )
                }) { paddingValues ->

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
                        player.setMediaItem(videoUri?.let { MediaItem.fromUri(it) }!!)
                        player.prepare()
                        player.play()

                        onDispose {
                            player.release()
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(paddingValues).background(Color.Black)
                    ) {

                        AndroidView(
                            factory = {
                                playerView
                            }, update = { //when user run app in background then do task
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
                            }, modifier = Modifier
                                .fillMaxSize()
//                .aspectRatio(16 / 9f)
                        )
                    }
                }
            }
        }
    }
}