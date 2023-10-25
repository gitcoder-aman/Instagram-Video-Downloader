package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SlowMotionVideo
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
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.tech.instasaver.MainActivity
import com.tech.instasaver.R
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.ui.theme.PinkColor
import com.tech.instasaver.ui.theme.Purple40
import com.tech.instasaver.viewmodel.FileListViewModel
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HistoryScreen() {

    val viewModel: FileListViewModel = viewModel()
    LaunchedEffect(viewModel) {
        viewModel.refreshFileList()
    }
    val listFileVideo by viewModel.listFileVideo.observeAsState(emptyList())
    val listFileImage by viewModel.listFileImage.observeAsState(emptyList())

    val context = LocalContext.current
    var isVideoSelected by rememberSaveable {
        mutableStateOf(true)
    }
    var isPhotoSelected by rememberSaveable {
        mutableStateOf(false)
    }

    var listFile: List<File> = emptyList()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFE8E8EC)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChipRow(
                chipName = "Video",
                chipIcon = Icons.Default.SlowMotionVideo,
                isSelected = isVideoSelected,
                onClick = {
                    isVideoSelected = true
                    isPhotoSelected = false
                })
            ChipRow(
                chipName = "Photo",
                chipIcon = Icons.Default.Photo,
                isSelected = isPhotoSelected,
                onClick = {
                    isVideoSelected = false
                    isPhotoSelected = true
                })
        }
        listFile = if(isVideoSelected){
            listFileVideo
        }else{
            listFileImage
        }

        if (listFile.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFFE8E8EC))
                    .fillMaxSize()
            ) {
                itemsIndexed(
                    items = listFile,
                    itemContent = { _, item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = expandVertically(),
                            exit = shrinkVertically(animationSpec = tween(durationMillis = 1000))
                        ) {
                            EachMediaFile(mediaFile = item) {
                                viewModel.removeFile(item, context)
                            }
                        }
                    }
                )
            }
        } else {
            Text(
                text = "No Any Media", style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = lato_bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            )
        }
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
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra("history_file_uri", mediaFile.toString())
            context.startActivity(intent)
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
            LoadImageWithGlide(mediaFile = mediaFile.toString()) //for video/Image

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
        }, colors = MenuDefaults.itemColors(textColor = Color.Black, leadingIconColor = PinkColor))
        Divider()
        DropdownMenuItem(text = {
            Text(text = "Delete")
        }, onClick = {
            isShowAlertDialog.value = true
        }, leadingIcon = {
            Icon(Icons.Default.Delete, contentDescription = null)
        }, colors = MenuDefaults.itemColors(textColor = Color.Black, leadingIconColor = PinkColor))
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
                        Text("Cancel", color = Purple40)
                    }
                    TextButton(
                        onClick = {
                            onDelete()
                            isShowAlertDialog.value = false
                            isDropDownMenu.value = false
                        }
                    ) {
                        Text("OK", color = Color.Red)
                    }
                }
            }
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoadImageWithGlide(
    mediaFile: String,
) {
    val painter = remember { mutableStateOf<Painter?>(null) }
    val context = LocalContext.current

    Glide.with(context)
        .asBitmap()
        .load(mediaFile)
        .placeholder(R.drawable.ic_launcher_foreground)
        .apply(RequestOptions().centerCrop())
        .listener(object : RequestListener<Bitmap?> {


            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap?>?,
                isFirstResource: Boolean
            ): Boolean {
                // Handle image load failure
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // Set the loaded image as the Painter
                painter.value = resource?.let { BitmapPainter(it.asImageBitmap()) }
                return false
            }
        })
        .submit()

    if (painter.value != null) {
        Image(painter = painter.value!!, contentDescription = null)
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
    }
}


