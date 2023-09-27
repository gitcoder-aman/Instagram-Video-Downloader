package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalClipboardManager
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.tech.instasaver.R
import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import com.tech.instasaver.apifetch_data.util.ApiState
import com.tech.instasaver.apifetch_data.viewModels.MainViewModel
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.customcomponents.CustomComponent
import com.tech.instasaver.ui.theme.PinkColor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun HomeScreen(navController: NavHostController) {

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
    val clipboardManager = LocalClipboardManager.current
    val mainViewModel: MainViewModel = hiltViewModel()

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
                    chipName = "Photo",
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
                    val clipboardContent = clipboardManager.getText()
                    urlText = ""
                    urlText = (clipboardContent ?: "").toString()
                    isGetData = false
                })
                Button(stringResource(R.string.download), onClick = {
                    isCheckUrl = true
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
                    navController
                )
            }
        }
        Text(
            text = "designed & developed by CoderAman",
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            style = TextStyle(
                fontWeight = FontWeight.W200,
                fontSize = 12.sp,
                fontFamily = lato_regular,
                fontStyle = FontStyle.Italic,
                color = PinkColor ,
                textAlign = TextAlign.Center
            )
        )
    }

}

@Composable
fun ChipRow(
    chipName: String,
    onClick: () -> Unit = {},
    chipIcon: ImageVector,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
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
                    "Change the type of Instagram Photo & Reel",
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
    isReelSelected: Boolean,
    navController: NavHostController
) {
    Log.d("@@@@main", "14 GETData: ${mainViewModel.response.value}")
    when (val result = mainViewModel.response.value) {
        is ApiState.Success -> {
            if (isReelSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.video_url != null) {
                    DownloadMedia(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected,
                        navController
                    )
                }
            } else if (isPhotoSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.display_url != null) {
                    DownloadMedia(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected,
                        navController
                    )
                }
            }
        }

        is ApiState.Failure -> {
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
private fun DownloadMedia(
    body: InstaModel,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean,
    navController: NavHostController
) {

    var downloadProgress by remember { mutableIntStateOf(0) }

    val uriLink: String = if (isReelSelected) {
        body.graphql.shortcode_media.video_url!!
    } else {
        body.graphql.shortcode_media.display_url!!
    }
    val STORAGE_DIRECTORY = "/Download/InstaSaver"
    val storageDirectory = if (isReelSelected) Environment.getExternalStorageDirectory()
        .toString() + STORAGE_DIRECTORY + "/${body.graphql.shortcode_media.id}" + ".mp4" else Environment.getExternalStorageDirectory()
        .toString() + STORAGE_DIRECTORY + "/${body.graphql.shortcode_media.id}" + ".jpg"

    //check file is created or not
    val file = File(Environment.getExternalStorageDirectory().toString() + STORAGE_DIRECTORY)
    if (!file.exists()) {
        file.mkdirs()
    }

    LaunchedEffect(key1 = Unit) {

        startDownloadTask(uriLink, storageDirectory) { progress ->
            downloadProgress = progress
        }
    }

    VideoDetailCard(
        body = body,
        downloadProgress = downloadProgress,
        isReelSelected = isReelSelected,
        isPhotoSelected = isPhotoSelected,
        navController = navController
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun startDownloadTask(
    uriLink: String,
    storageDirectory: String,
    progressCallback: (Int) -> Unit

) {

    GlobalScope.launch(Dispatchers.IO) {


        val url = URL(uriLink)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept-Encoding", "identity")
        connection.connect()

        if (connection.responseCode in 200..299) {
            val fileSize = connection.contentLength
            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(storageDirectory)

            var byteCopied: Long = 0
            var buffer = ByteArray(1024)
            var bytes = inputStream.read(buffer)
            while (bytes >= 0) {
                byteCopied += bytes
                var downloadProgress = (byteCopied.toFloat() / fileSize.toFloat() * 100).toInt()

                withContext(Dispatchers.Main) {
                    // Set the composable content within the withContext
                    Log.d("progressHome", "DownloadMedia: $downloadProgress")
                    progressCallback(downloadProgress)
                }
                outputStream.write(buffer, 0, bytes)
                bytes = inputStream.read(buffer)
            }
            outputStream.close()
            inputStream.close()
        }
    }
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
    isPhotoSelected: Boolean,
    navController: NavHostController
) {

    Card(
        onClick = { navController.navigate("videoPlayer/${Uri.encode(body.graphql.shortcode_media.video_url.toString())}") },
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
