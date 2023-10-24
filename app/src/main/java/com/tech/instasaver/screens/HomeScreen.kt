package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tech.instasaver.MainActivity.Companion.isDownloading
import com.tech.instasaver.R
import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import com.tech.instasaver.apifetch_data.util.ApiState
import com.tech.instasaver.apifetch_data.viewModels.MainViewModel
import com.tech.instasaver.common.AlertDialogShow
import com.tech.instasaver.common.getClipBoardData
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.customcomponents.CustomComponent
import com.tech.instasaver.downloadProcess.makeMediaFile
import com.tech.instasaver.downloadProcess.startDownloadTask
import com.tech.instasaver.model.MultipleData
import com.tech.instasaver.ui.theme.PinkColor
import com.tech.instasaver.util.InternetConnection.Companion.isNetworkAvailable
import com.tech.instasaver.util.Permission
import com.tech.instasaver.viewmodel.DialogViewModel
import kotlinx.coroutines.Job
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
    val alertDialogShowWhenSwitchType = remember { mutableStateOf(false) }

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
                        if (isDownloading) {
                            alertDialogShowWhenSwitchType.value = true
                        } else {
                            isReelSelected = true
                            isPhotoSelected = false
                            isGetData = false
                        }
                    })
                ChipRow(
                    chipName = "Photo/IGTV",
                    chipIcon = Icons.Default.Photo,
                    isSelected = isPhotoSelected,
                    onClick = {
                        if (isDownloading) {
                            alertDialogShowWhenSwitchType.value = true
                        } else {
                            isPhotoSelected = true
                            isReelSelected = false
                            isGetData = false
                        }
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
                    if (isDownloading) {
                        alertDialogShowWhenSwitchType.value = true
                    } else {
                        urlText = getClipBoardData(context = context)
                        isGetData = false
                    }
                })
                Button(stringResource(R.string.download), onClick = {
                    if (isDownloading) {
                        alertDialogShowWhenSwitchType.value = true
                    } else {

                        val permission = Permission()
                        if (isNetworkAvailable(context)) {
                            isCheckUrl =
                                if (permission.checkStoragePermission(context as Activity)) {
                                    true
                                } else {
                                    Toast.makeText(
                                        context,
                                        "please provide the storage permission.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    permission.requestPermission(context)
                                    false
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Please Check your internet..",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isCheckUrl = false
                        }
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

            if (alertDialogShowWhenSwitchType.value) {
                AlertDialogShow(alertDialogShowWhenSwitchType)
            }
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

                isDownloading = true
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
            text = stringResource(R.string.designed_developed_by_coderaman),
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
    val context = LocalContext.current
    when (val result = mainViewModel.response.value) {
        is ApiState.Success -> {
            Log.d("@@@@main", "14 GETData: ${result}")
            if (isReelSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.video_url != null) {
                    GetLinkAndDownload(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong.${result.data.body()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    isDownloading = false
                }
            } else if (isPhotoSelected) {
                if (result.data.body()?.graphql?.shortcode_media?.display_url != null) {
                    Log.d("groupSingleLink", "14 GETData: ${result.data}")

                    GetLinkAndDownload(
                        result.data.body()!!,
                        isReelSelected,
                        isPhotoSelected
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong.${result.data.body()?.graphql?.shortcode_media?.display_url}",
                        Toast.LENGTH_SHORT
                    ).show()
                    isDownloading = false
                }
            }
        }

        is ApiState.Failure -> {
            Toast.makeText(context, "Something went wrong with this Link.", Toast.LENGTH_SHORT)
                .show()
            isDownloading = false
            Log.d("@@@@main", "GETData:  ${result.msg}")
        }

        is ApiState.Loading -> {
            CircularProgressIndicator(color = PinkColor, strokeWidth = 2.dp)
            Log.d("@@@@main", "GETData: ${"Loading"}")
        }

        is ApiState.Empty -> {
            isDownloading = false
            Toast.makeText(LocalContext.current, "Video Url is not available", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
private fun GetLinkAndDownload(
    body: InstaModel,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean
) {

    var downloadProgress by remember { mutableIntStateOf(0) }
    var uriLink: String = ""
    var groupSingleIGTVLink: String = ""
    var groupSinglePicLink: String = ""
    var startDownload by remember { mutableStateOf(true) }
    var downloadLinkList = mutableListOf<MultipleData>()


    val fileMovie = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/Movies/InstaSaver"
    )
    if (!fileMovie.exists()) {
        fileMovie.mkdirs()
    }
    //for picture
    val filePic = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/Pictures/InstaSaver"
    )
    if (!filePic.exists()) {
        filePic.mkdirs()
    }
    if (isReelSelected) {
        uriLink = body.graphql.shortcode_media.video_url!!
    } else {

        if (body.graphql.shortcode_media.edge_sidecar_to_children?.edges?.isNotEmpty() == true) {
            startDownload = false
            //for IGTV Movies
            for (i in 0 until body.graphql.shortcode_media.edge_sidecar_to_children.edges.size) {

                if (body.graphql.shortcode_media.edge_sidecar_to_children.edges[i].node.is_video) {
                    groupSingleIGTVLink =
                        body.graphql.shortcode_media.edge_sidecar_to_children.edges[i].node.video_url

                    val title =
                        body.graphql.shortcode_media.edge_media_to_caption?.edges?.get(0)?.node?.text!!

                    if (groupSingleIGTVLink.isNotEmpty()) {

                        val makeNewStorageDirectory = Environment.getExternalStorageDirectory()
                            .toString() + "/Movies/InstaSaver" + "/${"insta" + System.currentTimeMillis()}" + ".mp4"
                        val singleData = MultipleData(
                            groupSingleIGTVLink,
                            title,
                            body.graphql.shortcode_media.owner?.username!!,
                            makeNewStorageDirectory
                        )
                        downloadLinkList.add(singleData)
                    }
                }

                //for Image get
                if (body.graphql.shortcode_media.edge_sidecar_to_children.edges[i].node.display_resources.isNotEmpty()) {
                    groupSinglePicLink =
                        body.graphql.shortcode_media.edge_sidecar_to_children.edges[i].node.display_resources[0].src

                    val title =
                        body.graphql.shortcode_media.edge_media_to_caption?.edges?.get(0)?.node?.text!!

                    if (groupSinglePicLink.isNotEmpty()) {
                        val makeNewStorageDirectory = Environment.getExternalStorageDirectory()
                            .toString() + "/Pictures/InstaSaver" + "/${"insta" + System.currentTimeMillis()}" + ".jpg"

                        val singleData = MultipleData(
                            groupSinglePicLink,
                            title,
                            body.graphql.shortcode_media.owner?.username!!,
                            makeNewStorageDirectory
                        )
                        downloadLinkList.add(singleData)
                    }
                }
            }
            val dialogViewModel: DialogViewModel = viewModel()
            dialogViewModel.downloadLinkList(downloadLinkList)
            DialogScreen(viewModel = dialogViewModel)


//                    if (groupLinkHashmap.size > 0) {
//                        Log.d("hashmap@@", "GetLinkAndDownload: ${groupLinkHashmap.size}")
//                        for ((link, path) in groupLinkHashmap) {
//                            Log.d("hashmap@@", "GetLinkAndDownload: $link")
//                            Log.d("hashmap@@", "GetLinkAndDownload: $path")
//
//                            LaunchedEffect(key1 = Unit) {
//                                val downloadJob = CoroutineScope(Dispatchers.IO).launch {
//                                    startDownloadTask(link, path) { progress ->
//                                        downloadProgress = progress
//                                    }
//                                }
//                                downloadJobs.add(downloadJob)
//                            }
//                        }
//                        CoroutineScope(Dispatchers.Main).launch {
//                            downloadJobs.forEach { job ->
//                                job.join()
//                            }
//                        }
//                    }
//                } else {
//                    uriLink = body.graphql.shortcode_media.display_url!!
//                }

        } else {

            if (body.graphql.shortcode_media.video_url != null) {
                uriLink = body.graphql.shortcode_media.video_url
            } else if (body.graphql.shortcode_media.display_url != null) {
                uriLink = body.graphql.shortcode_media.display_url
            }

        }

    }

    if ((uriLink == "") || downloadProgress == 100) {
        isDownloading = false
    }

    if (startDownload) {
        val storageDirectory = makeMediaFile(body, isReelSelected, isPhotoSelected)
        LaunchedEffect(Unit) {
            startDownloadTask(uriLink, storageDirectory) { progress ->
                downloadProgress = progress
            }
        }
    }

    if (startDownload) {
        VideoDetailCard(
            body = body,
            downloadProgress = downloadProgress,
            isReelSelected = isReelSelected,
            isPhotoSelected = isPhotoSelected
        )
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
