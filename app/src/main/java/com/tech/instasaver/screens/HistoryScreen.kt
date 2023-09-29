package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.tech.instasaver.R
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.ui.theme.PinkColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun HistoryScreen(navController: NavHostController) {

    val targetPath: String =
        Environment.getExternalStorageDirectory().absolutePath + "/Download/InstaSaver/"
    val mediaDirectory = File(targetPath)
    var mediaFiles by remember { mutableStateOf(listOf<File>()) }

    mediaFiles = mediaDirectory.listFiles()?.toList() ?: emptyList()

    Log.d("file@@", "HistoryScreen: ${mediaFiles.size}")
    LazyColumn {
        items(mediaFiles) { mediaFile ->
            if (mediaFile.isFile && (mediaFile.extension.equals(
                    "mp4",
                    ignoreCase = true
                ) || mediaFile.extension.equals("jpg", ignoreCase = true))
            ) {
                EachMediaFile(mediaFile, navController)
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachMediaFile(
    mediaFile: File,
    navController: NavHostController,
) {

    Log.d("file@@", "EachMediaFile: ${Uri.fromFile(mediaFile)}")
    Card(
        onClick = {
            //passing the video uri through navigation
            if (mediaFile.extension.equals("mp4", ignoreCase = true)) {
                navController.navigate(
                    "videoPlayer/${Uri.encode(Uri.fromFile(mediaFile).toString())}"
                )
            }
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp, 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = PinkColor,
            contentColor = Color.Black
        ),
        border = BorderStroke(
            1.dp,
            Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mediaFile.extension.equals(
                    "mp4",
                    ignoreCase = true
                )
            ) {
                SetVideoThumbnail(mediaFile = mediaFile)
            } else {
                AsyncImage(
                    model = Uri.fromFile(mediaFile),
                    contentDescription = "insta_downloader",
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier.fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = mediaFile.name, style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = lato_bold,
                        color = Color.Black
                    )
                )
                //media Title
                Text(
                    text = mediaFile.absolutePath,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = lato_regular,
                        color = Color.Black,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SetVideoThumbnail(mediaFile: File) {
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    CoroutineScope(Dispatchers.IO).launch {
        val retriever =
            MediaMetadataRetriever()  //mediaMetaDataRetriever is getting video frame

        try {
            val uri = Uri.fromFile(mediaFile)
            retriever.setDataSource(context, uri)

            // Retrieve the video's first frame as a bitmap
            val frame = retriever.getFrameAtTime(0)

            thumbnailBitmap = frame
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }

//        onDispose {
//            // Clean up any resources here if needed
//        }
    }
    thumbnailBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight(),
            contentScale = ContentScale.Fit
        )
    }
}

