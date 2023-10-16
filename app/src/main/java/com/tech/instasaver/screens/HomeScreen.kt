package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tech.instasaver.R
import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import com.tech.instasaver.apifetch_data.util.ApiState
import com.tech.instasaver.apifetch_data.viewModels.MainViewModel
import com.tech.instasaver.common.getClipBoardData
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.customcomponents.CustomComponent
import com.tech.instasaver.downloadFile.startDownloadTask
import com.tech.instasaver.ui.theme.PinkColor
import com.tech.instasaver.util.InternetConnection.Companion.isNetworkAvailable
import com.tech.instasaver.util.Permission
import java.io.File

@Composable
fun HomeScreen(receiverText: String) {

    val context = LocalContext.current
    var urlText by rememberSaveable {
        mutableStateOf("")
    }
    var isGetData by rememberSaveable {
        mutableStateOf(false)
    }
    var instaIdGet by rememberSaveable {
        mutableStateOf("")
    }
    var isCheckUrl by rememberSaveable {
        mutableStateOf(false)
    }
    var isFetchingData by rememberSaveable {
        mutableStateOf(false)
    }
    var isPhotoSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var isReelSelected by rememberSaveable {
        mutableStateOf(true)
    }
    val mainViewModel: MainViewModel = hiltViewModel()

    if (getClipBoardData(context) == "") {
        if (receiverText != "") {
            Log.d("intent@@", "!HomeScreen: $receiverText")
            urlText = receiverText
            Log.d("intent@@", "!HomeScreen: $urlText")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                ChipRow(
                    chipName = "Reel",
                    chipIcon = Icons.Default.SlowMotionVideo,
                    isSelected = isReelSelected,
                    onClick = {
                        isReelSelected = true
                        isPhotoSelected = false
                    })
                ChipRow(
                    chipName = "Photo/IGTV",
                    chipIcon = Icons.Default.Photo,
                    isSelected = isPhotoSelected,
                    onClick = {
                        isPhotoSelected = true
                        isReelSelected = false
                    })
            }

            TextFieldLayout(text = urlText)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                Button(stringResource(id = R.string.paste_link), onClick = {
                    urlText = getClipBoardData(context = context)
                    isGetData = false
                })
                Button(stringResource(R.string.download), onClick = {
                    val permission = Permission()
                    if (isNetworkAvailable(context)) {
                        if (permission.checkStoragePermission(context as Activity)) {
                            isCheckUrl = true
                        } else {
                            Toast.makeText(
                                context,
                                "please provide the storage permission.",
                                Toast.LENGTH_SHORT
                            ).show()
                            permission.requestPermission(context as Activity)
                            isCheckUrl = false
                        }
                    } else {
                        Toast.makeText(context, "Please Check your internet..", Toast.LENGTH_SHORT)
                            .show()
                        isCheckUrl = false
                    }
                })

                if (isCheckUrl) {
                    instaIdGet = instaIdGet(urlText, isPhotoSelected, isReelSelected)
                    Log.d("@@@@main", "3 $instaIdGet")
                    if (instaIdGet != "") {
                        isFetchingData = true
                    }
                    isCheckUrl = false
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (isFetchingData) {
                if (isReelSelected) {
                    Log.d("@@@@main", "$isReelSelected")
                    mainViewModel.fetchInstaVideo(instaIdGet)
                } else if (isPhotoSelected) {
                    Log.d("@@@@main", "$isPhotoSelected")
                    mainViewModel.fetchInstaPhoto(instaIdGet)
                }
                isGetData = true
                isFetchingData = false
            }
            if (isGetData) {

                Log.d("@@@@main", "13$mainViewModel")
                Log.d("@@@@main", "isPhoto$isPhotoSelected")
                Log.d("@@@@main", "isReel$isReelSelected")
                GETData(
                    mainViewModel = mainViewModel,
                    isPhotoSelected,
                    isReelSelected,
                )
            }
        }
        Text(
            text = "designed & developed by CoderAman",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = TextStyle(
                fontWeight = FontWeight.W200,
                fontSize = 12.sp,
                fontFamily = lato_regular,
                fontStyle = FontStyle.Italic,
                color = PinkColor,
                textAlign = TextAlign.Center
            )
        )
    }

}

@Composable
fun ChipRow(
    chipName: String,
    onClick: () -> Unit,
    chipIcon: ImageVector,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Black, shape = CircleShape)
            .background(if (isSelected) PinkColor else Color.White)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = chipName,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = chipIcon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Black,
        )
    }
}

@Composable
fun instaIdGet(urlText: String, isPhotoSelected: Boolean, isReelSelected: Boolean): String {
    if (urlText.isNotEmpty()) {
        if (urlText.matches("https://www.instagram.com/(.*)".toRegex())) {
            if (urlText.contains("https://www.instagram.com/reel") && isReelSelected) {
                val splitReelId = urlText.substring(31, 42)
                if (splitReelId.length >= 10) {
                    return splitReelId
                }
            } else if (urlText.contains("https://www.instagram.com/p") && isPhotoSelected) {
                val slipPhotoId = urlText.substring(28, 39)
                if (slipPhotoId.length >= 10) {
                    return slipPhotoId
                }
            } else {
                Toast.makeText(
                    LocalContext.current,
                    "Change the type of Instagram Photo/IGTV & Reel",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                LocalContext.current,
                "only support for instagram link.",
                Toast.LENGTH_SHORT
            ).show()
        }
    } else {
        Toast.makeText(
            LocalContext.current,
            "first paste the instagram link.",
            Toast.LENGTH_SHORT
        ).show()
        return ""
    }
    return ""
}

@Composable
private fun GETData(
    mainViewModel: MainViewModel,
    isPhotoSelected: Boolean,
    isReelSelected: Boolean
) {
    Log.d("@@@@main", "14 GETData: ${mainViewModel.response.value}")
    val context = LocalContext.current
    when (val result = mainViewModel.response.value) {
        is ApiState.Success -> {
            if (isReelSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.video_url != null) {
                    MakeMediaFile(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected
                    )
                }
            } else if (isPhotoSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.display_url != null) {
                    MakeMediaFile(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected
                    )
                }
            }
        }

        is ApiState.Failure -> {
            Toast.makeText(context, "Something went wrong with this Link.", Toast.LENGTH_SHORT)
                .show()
            Log.d("@@@@main", "GETData:  ${result.msg}")
        }

        is ApiState.Loading -> {
            CircularProgressIndicator(color = PinkColor, strokeWidth = 2.dp)
            Log.d("@@@@main", "GETData: ${"Loading"}")
        }

        is ApiState.Empty -> {
            Toast.makeText(LocalContext.current, "Video Url is not available", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
private fun MakeMediaFile(
    body: InstaModel,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean
) {

    var downloadProgress by remember { mutableIntStateOf(0) }

    val uriLink: String = if (isReelSelected) {
        body.graphql.shortcode_media.video_url!!
    } else {
        if (body.graphql.shortcode_media.edge_sidecar_to_children?.edges?.isNotEmpty() == true && body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.is_video) {
            body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.video_url
        } else {
            body.graphql.shortcode_media.display_url!!
        }
    }
    val storageDirectory = if (isReelSelected) {
        val STORAGE_DIRECTORY_FOR_VIDEO = "/Movies/InstaSaver"
        //check file is created or not
        val file = File(Environment.getExternalStorageDirectory().toString() + STORAGE_DIRECTORY_FOR_VIDEO)
        if (!file.exists()) {
            file.mkdirs()
        }
        Environment.getExternalStorageDirectory()
            .toString() + STORAGE_DIRECTORY_FOR_VIDEO + "/${body.graphql.shortcode_media.id}" + ".mp4"
    }
    else {
        var extension = ".jpg"
        var STORAGE_DIRECTORY_FOR_PICTURE_IGTV = "/Pictures/InstaSaver"

        if (body.graphql.shortcode_media.edge_sidecar_to_children?.edges?.isNotEmpty() == true && body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.is_video) {
            extension = ".mp4"
            STORAGE_DIRECTORY_FOR_PICTURE_IGTV = "/Movies/InstaSaver"
        }
        val file = File(Environment.getExternalStorageDirectory().toString() + STORAGE_DIRECTORY_FOR_PICTURE_IGTV)
        if (!file.exists()) {
            file.mkdirs()
        }
        Environment.getExternalStorageDirectory()
            .toString() + STORAGE_DIRECTORY_FOR_PICTURE_IGTV + "/${body.graphql.shortcode_media.id}" + extension
    }

    LaunchedEffect(Unit) {
        startDownloadTask(uriLink, storageDirectory) { progress ->
            downloadProgress = progress
        }
    }

    VideoDetailCard(
        body = body,
        downloadProgress = downloadProgress,
        isReelSelected = isReelSelected,
        isPhotoSelected = isPhotoSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldLayout(text: String) {
    TextField(
        value = text, onValueChange = {}, modifier = Modifier
            .padding(15.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(fontFamily = lato_regular),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White,
            cursorColor = PinkColor,
            focusedTrailingIconColor = Color.Black,
            selectionColors = TextSelectionColors(
                handleColor = PinkColor,
                backgroundColor = PinkColor
            )
        ), placeholder = {
            Text(
                text = stringResource(R.string.paste_instagram_link_here), style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W300,
                    fontFamily = lato_regular
                )
            )
        }, readOnly = true
    )
}

@Composable
fun Button(buttonText: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = PinkColor,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Text(
            text = buttonText, style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.W300,
                fontFamily = lato_regular
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailCard(
    body: InstaModel,
    downloadProgress: Int,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean
) {

    val context = LocalContext.current
    val uriLink: String = if (isReelSelected) {
        body.graphql.shortcode_media.video_url!!
    } else {
        if (body.graphql.shortcode_media.edge_sidecar_to_children?.edges?.isNotEmpty() == true && body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.is_video) {
            body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.video_url
        } else {
            body.graphql.shortcode_media.display_url!!
        }
    }
    Card(
        onClick = {
//                navController.navigate("videoPlayer/${Uri.encode(uriLink)}")
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra("home_file_url", uriLink)
            context.startActivity(intent)
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp, 1.dp),
        colors = CardDefaults.cardColors(containerColor = PinkColor, contentColor = Color.Black),
        border = BorderStroke(
            1.dp,
            Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = CenterVertically
        ) {
            ImageWithDownloaderView(body, downloadProgress, isReelSelected, isPhotoSelected)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterVertically)
                    .padding(start = 5.dp, end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = CenterVertically
                ) {
                    AsyncImage(
                        model = body.graphql.shortcode_media.owner?.profile_pic_url,
                        contentDescription = "insta_downloader",
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        error = painterResource(id = R.drawable.ic_launcher_background),
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = body.graphql.shortcode_media.owner?.username!!, style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = lato_bold,
                            color = Color.Black
                        )
                    )
                }
                //media Title
                Text(
                    text = (if (body.graphql.shortcode_media.edge_media_to_caption?.edges?.isEmpty() == true) body.graphql.shortcode_media.shortcode else body.graphql.shortcode_media.edge_media_to_caption?.edges?.get(
                        0
                    )?.node?.text)!!,
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

@Composable
fun ImageWithDownloaderView(
    body: InstaModel,
    downloadProgress: Int,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean
) {
    Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {

        AsyncImage(
            model = if (isReelSelected) body.graphql.shortcode_media.thumbnail_src else if (isPhotoSelected) body.graphql.shortcode_media.display_url else {
            },
            contentDescription = "insta_downloader",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier.width(100.dp)
        )
        CustomComponent(
            canvasSize = 100.dp,
            indicatorValue = downloadProgress,
            backgroundIndicatorColor = Color.White,
            backgroundIndicatorStrokeWidth = 20f,
            foregroundIndicatorStrokeWidth = 10f,
            foregroundIndicatorColor = PinkColor,
            bigTextColor = PinkColor,
            bigTextFontSize = 20.sp,
        )
    }
}
