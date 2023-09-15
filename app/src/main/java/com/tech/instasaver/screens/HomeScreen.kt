package com.tech.instasaver.screens

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tech.instasaver.R
import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import com.tech.instasaver.apifetch_data.util.ApiState
import com.tech.instasaver.apifetch_data.viewModels.MainViewModel
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.customcomponents.CustomComponent
import com.tech.instasaver.ui.theme.PinkColor

@Composable
fun HomeScreen() {

    var urlText by remember {
        mutableStateOf("")
    }
    var isGetData by remember {
        mutableStateOf(false)
    }
    var reelId by remember {
        mutableStateOf("")
    }
    var isCheckUrl by remember {
        mutableStateOf(false)
    }
    var downloadValue by remember {
        mutableFloatStateOf(0F)
    }

    val clipboardManager = LocalClipboardManager.current
    val mainViewModel: MainViewModel = hiltViewModel()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            TextFieldLayout(text = urlText)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(stringResource(id = R.string.paste_link), onClick = {
                    val clipboardContent = clipboardManager.getText()
                    urlText = ""
                    urlText = (clipboardContent ?: "").toString()
                    isGetData = false
                    isCheckUrl = true
                    Log.d("@@@@main", "1$isCheckUrl")
                })
                Button(stringResource(R.string.download), onClick = {
                    Log.d("@@@@main", "5$reelId")
                    if (reelId != "") {
                        mainViewModel.fetchInstaVideo(reelId)
                        isGetData = true
                        Log.d("@@@@main", "6$isGetData")
                        Log.d("@@@@main", "7$reelId")
                    }
                })

                if (isCheckUrl) {
                    Log.d("@@@@main", "2$isCheckUrl")
                    reelId = reelIdGet(urlText)
                    Log.d("@@@@main", "3$reelId")
                    isCheckUrl = false
                    Log.d("@@@@main", "4$isCheckUrl")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (isGetData) {

                Log.d("@@@@main", "13$mainViewModel")

                GETData(mainViewModel = mainViewModel)
            }
        }
    }

}

@Composable
fun reelIdGet(urlText: String): String {
    if (urlText.isNotEmpty()) {
        if (urlText.matches("https://www.instagram.com/(.*)".toRegex())) {
            val splitReelId = urlText.substring(31, 42)
            if (splitReelId.length >= 10) {
                return splitReelId
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
fun GETData(mainViewModel: MainViewModel) {
    Log.d("@@@@main", "14 GETData: ${mainViewModel.response.value}")
    when (val result = mainViewModel.response.value) {
        is ApiState.Success -> {
            if (result.data.body()?.graphql?.shortcode_media?.video_url != null) {
                DownloadMedia(result.data.body()!!) {

                }
            }
        }

        is ApiState.Failure -> {
            Log.d("@@@@main", "GETData: ${result.msg}")
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

@Composable
private fun DownloadMedia(body: InstaModel, onValueChange: () -> Unit) {

    val scanUri = "content://downloads/instasaver"
    val videoLink = body.graphql.shortcode_media.video_url
    val downloadManager =
        LocalContext.current.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val request = DownloadManager.Request(Uri.parse(videoLink)).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        setTitle("Downloading Video")
        setDescription("Please Wait..")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, body.graphql.shortcode_media.id + ".mp4"
        )

    }
    val id = downloadManager.enqueue(request)
    val downloadProgress = updateDownloadProgress(id,downloadManager)
    /*val query = DownloadManager.Query().setFilterById(id)
    val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)

            val cursor = downloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                val download =
                    cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val total =
                    cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                percentage = (((download * 100) / total).toInt())

                Log.d("percentage", "onChange: ${percentage}")

            }
        }
    }*/
//    LocalContext.current.contentResolver.registerContentObserver(Uri.parse(scanUri), true, observer)
    Log.d("percentage", "onChange: ${downloadProgress}")
    VideoDetailCard(body, downloadProgress)
}

@Composable
private fun updateDownloadProgress(downloadId: Long, downloadManager: DownloadManager) : Int {
    var downloadProgress by remember {
        mutableIntStateOf(0)
    }
    var progress = 0
    while (progress < 100) {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    progress = 100
                    downloadProgress = progress
                }

                DownloadManager.STATUS_FAILED -> {
                    progress = -1
                    downloadProgress = progress
                }

                else -> {
                    val downloadedBytes =
                        cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val totalBytes =
                        cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    progress = ((downloadedBytes.toFloat() / totalBytes) * 100).toInt()
                    downloadProgress = progress
                }
            }
        }
        cursor.close()
    }
    return downloadProgress
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
fun VideoDetailCard(body: InstaModel, downloadProgress: Int) {

    Card(
        onClick = { /*TODO*/ },
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
            ImageWithDownloaderView(body, downloadProgress)
            Column(
                modifier = Modifier
                    .fillMaxWidth().align(CenterVertically)
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
                Text(
                    text = body.graphql.shortcode_media.edge_media_to_caption?.edges!![0].node.text,
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
fun ImageWithDownloaderView(body: InstaModel, downloadProgress: Int) {

    Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {

        AsyncImage(
            model = body.graphql.shortcode_media.thumbnail_src,
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
