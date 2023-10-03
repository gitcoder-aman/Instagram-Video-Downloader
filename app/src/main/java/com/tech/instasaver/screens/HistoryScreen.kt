package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun HistoryScreen() {

    val viewModel: FileListViewModel = viewModel()
    viewModel.getMediaFile()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .background(PinkColor)
            .fillMaxSize()
    ) {
        itemsIndexed(
            items = viewModel.listMedia,
            itemContent = { _, item ->
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 1000))
                ) {
                    EachMediaFile(mediaFile = item) {
                        if (item.exists() && item.isFile) {
                            if (item.delete()) {
                                viewModel.removeItem(item)
                                Toast.makeText(
                                    context,
                                    "Video is Deleted from Device.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
            }
        )
    }
}

@SuppressLint("QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachMediaFile(
    mediaFile: File,
    onDelete: () -> Unit
) {

    val context = LocalContext.current
    val isDropDownMenu = remember { mutableStateOf(false) }
    val localContext = LocalDensity.current
    var anchorPosition by remember { mutableStateOf<Offset?>(null) }


    Log.d("file@@", "EachMediaFile: ${Uri.fromFile(mediaFile)}")
    Card(
        onClick = {
            //passing the video uri through navigation
            if (mediaFile.extension.equals("mp4", ignoreCase = true)) {
//                navController.navigate(
//                    "videoPlayer/${Uri.encode(Uri.fromFile(mediaFile).toString())}"
//                )
                val intent = Intent(context, VideoPlayerActivity::class.java)
                intent.putExtra("history_video_uri", mediaFile.toString())
                context.startActivity(intent)
            }
        },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp, 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
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
                .weight(1f)
                .padding(4.dp),
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
                    placeholder = painterResource(id = R.drawable.instagram_seeklogo_com),
                    error = painterResource(id = R.drawable.instagram_seeklogo_com),
                    modifier = Modifier.fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(0.3f)) {
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

            val toggleMenu: () -> Unit = {
                isDropDownMenu.value = !isDropDownMenu.value
                if (isDropDownMenu.value) {
                    val density = localContext.density
                    val position = Offset(
                        (-16f * density), // Adjust this offset as needed
                        0f
                    )
                    anchorPosition = position
                }
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    toggleMenu()
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
        if (isDropDownMenu.value) {
            DropDownMenu(isDropDownMenu, anchorPosition, mediaFile) {
                onDelete()
            }
        }
    }
}

@Composable
fun DropDownMenu(
    isDropDownMenu: MutableState<Boolean>,
    anchorPosition: Offset?,
    mediaFile: File,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val isShowAlertDialog = remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = isDropDownMenu.value,
        onDismissRequest = { isDropDownMenu.value = false },
        offset = anchorPosition?.let { DpOffset(it.x.dp, it.y.dp) } ?: DpOffset(
            0.dp,
            0.dp
        ), // Apply the offset
        modifier = Modifier.background(Color.White)
    ) {
        DropdownMenuItem(text = {
            Text(text = "Share")
        }, onClick = {
            Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show()
            val contentUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                mediaFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "video/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(shareIntent)
            isDropDownMenu.value = false
        }, leadingIcon = {
            Icon(Icons.Default.Share, contentDescription = null)
        })
        Divider()
        DropdownMenuItem(text = {
            Text(text = "Delete")
        }, onClick = {
            isShowAlertDialog.value = true
        }, leadingIcon = {
            Icon(Icons.Default.Delete, contentDescription = null)
        })
    }
    if (isShowAlertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                isShowAlertDialog.value = false
            },
            title = {
                Row {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Delete")
                }
            },
            text = {
                Text(text = "Are you sure delete this video?")
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            isShowAlertDialog.value = false
                            isDropDownMenu.value = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onDelete()
                            isShowAlertDialog.value = false
                            isDropDownMenu.value = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        )
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

