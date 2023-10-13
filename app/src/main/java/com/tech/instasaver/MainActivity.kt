package com.tech.instasaver

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tech.instasaver.common.clearClipBoardData
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.navigation.TabItem
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen
import com.tech.instasaver.screens.HowToUseActivity
import com.tech.instasaver.ui.theme.InstaSaverTheme
import com.tech.instasaver.ui.theme.PinkColor
import com.tech.instasaver.util.InAppReview
import com.tech.instasaver.util.InternetConnection.Companion.isNetworkAvailable
import com.tech.instasaver.util.Permission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var receiverText by mutableStateOf("") // Initial value

    companion object {
        const val UPDATE_REQUEST_CODE = 123
        const val PERMISSIONS_REQUEST_STORAGE = 100
    }

    private var appUpdateManager: AppUpdateManager ?= null
    private val updateType = AppUpdateType.IMMEDIATE

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (isNetworkAvailable(this)) {

            if (intent != null) { //when second time open app then change receiver text
                val newIntentReceiverText = checkIntentValue(intent)
                receiverText = newIntentReceiverText ?: ""
                Log.d("intent@@", "onNewIntent: $newIntentReceiverText")
                clearClipBoardData(this)
            }
        } else {
            Toast.makeText(this, "Please check Internet..", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (isNetworkAvailable(this)) {

            if (intent != null) { //when second time open app then change receiver text
                val newIntentReceiverText = checkIntentValue(intent)
                receiverText = newIntentReceiverText ?: ""
                Log.d("intent@@", "onNewIntent: $newIntentReceiverText")
                clearClipBoardData(this)
            }
            appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

            appUpdateManager!!.registerListener(installStateUpdatedListener)
            checkForAppUpdate()

        } else {
            Toast.makeText(this, "Please check internet..", Toast.LENGTH_LONG).show()
        }

        val permission = Permission()  //provide the storage permission
        // Check for permissions and request them if not granted
        if (permission.checkStoragePermission(this)) {
            // Permissions are already granted, you can access files here
            // Perform file-related operations here
        } else {
            // Permissions are not granted, request them
            permission.requestPermission(this)
        }
        setContent {
            InstaSaverTheme {

                val scaffoldState = rememberScaffoldState()

                Scaffold(topBar = {
                    TopBar(scaffoldState)
                }, scaffoldState = scaffoldState, drawerContent = {
                    DrawerContent(scaffoldState)
                }, drawerGesturesEnabled = true) { padding ->
                    Column(
                        modifier = Modifier.padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (receiverText != "") {
                            Log.d("intent@@", "!onCreate: $receiverText")
                            TabScreen(receiverText = receiverText)
                        } else {
                            Log.d("intent@@", "onCreate: $receiverText")
                            TabScreen("")
                        }
                    }
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access files now
                // Perform file-related operations here
                Log.d("permission@@", "onRequestPermissionsResult: Permission Granted")
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Please provide the storage permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkForAppUpdate() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager!!.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    UPDATE_REQUEST_CODE
                )
            }

        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    UPDATE_REQUEST_CODE
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager?.unregisterListener(installStateUpdatedListener)
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                this,
                "Download successful,Restarting app in 5 seconds",
                Toast.LENGTH_LONG
            ).show()
        }
        lifecycleScope.launch {
            delay(5000)
            appUpdateManager?.completeUpdate()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Something went wrong updating.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun DrawerContent(scaffoldState: ScaffoldState) {
    var isSharing by remember {
        mutableStateOf(false)
    }
    var isFeedback by remember {
        mutableStateOf(false)
    }
    var isHowToUse by remember {
        mutableStateOf(false)
    }
    var isRating by remember {
        mutableStateOf(false)
    }
    var isPrivacy by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        DrawerHeader()
        Divider(color = Color.Gray, thickness = 1.dp)

        // Drawer items
        DrawerItem(icon = Icons.Default.QuestionMark, title = stringResource(R.string.how_to_use)) {
            isHowToUse = true
        }
        Divider(color = Color.Gray, thickness = 0.2.dp)

        DrawerItem(icon = Icons.Default.Share, title = stringResource(R.string.share)) {
            isSharing = true
        }
        Divider(color = Color.Gray, thickness = 0.2.dp)

        DrawerItem(icon = Icons.Default.Feedback, title = stringResource(R.string.feedback)) {
            isFeedback = true
        }
        Divider(color = Color.Gray, thickness = 0.2.dp)

        DrawerItem(icon = Icons.Default.StarRate, title = stringResource(R.string.rate_the_app)) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName))
            startActivity(context, intent, null)
            isRating = true
        }
        Divider(color = Color.Gray, thickness = 0.2.dp)

        DrawerItem(icon = Icons.Default.PrivacyTip, title = stringResource(R.string.privacy)) {
            isPrivacy = true
        }
        Divider(color = Color.Gray, thickness = 0.2.dp)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "App version-${stringResource(id = R.string.app_version)}",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W200,
                    fontFamily = FontFamily.Monospace,
                    fontStyle = FontStyle.Italic, textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    if (isRating) {
        val inAppReview = InAppReview()
        inAppReview.askUserForReview(context as Activity)
        isRating = false
    }
    if (isSharing) {
        LaunchedEffect(true) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this amazing app!\nAn Instagram video downloader is a tool or application that " +
                            "allows users to save Instagram videos to their device, enabling offline viewing or sharing " +
                            "outside the Instagram platform" + "here link-> ${"https://play.google.com/store/apps/details?id=${context.packageName}"}"
                )
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share via")
            context.startActivity(chooserIntent)
            isSharing = false
        }
    }
    if (isFeedback) {
        // Inside your activity or Composable function
        val recipientEmail =
            stringResource(R.string.my_gmail) // Replace with the desired email address
        val emailUri = Uri.parse("mailto:$recipientEmail")
        val intent = Intent(Intent.ACTION_VIEW, emailUri)
        intent.setPackage("com.google.android.gm") // Specify the package name of the Gmail app

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handle the case where the Gmail app is not installed or other errors
            Toast.makeText(context, "something went wrong.", Toast.LENGTH_SHORT).show()
        }
        isFeedback = false
    }
    if (isHowToUse) {
        val intent = Intent(context, HowToUseActivity()::class.java)
        context.startActivity(intent)
//        navController.navigate(HowToUse)
        LaunchedEffect(Unit) {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
        isHowToUse = false
    }
    if (isPrivacy) {
        val url = stringResource(R.string.privacy_policy_link)

        // Create an Intent to open a web browser
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        // Start the web browser activity
        context.startActivity(intent)
        isPrivacy = false
    }
}

@Composable
fun DrawerHeader() {
    // Customize the header layout as needed
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = stringResource(R.string.insta_saver), style = typography.h6)
    }
}

@Composable
fun DrawerItem(icon: ImageVector, title: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = PinkColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun TopBar(scaffoldState: ScaffoldState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(), backgroundColor = PinkColor, elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to open the drawer
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
            Text(
                text = stringResource(id = R.string.app_name), style = TextStyle(
                    fontFamily = lato_regular,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.instagram_seeklogo_com),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://www.instagram.com/")
                            intent.setPackage("com.instagram.android") // Instagram package name
                            context.startActivity(intent, null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            )
        }
    }
}

fun checkIntentValue(intent: Intent): String? {

    var receivedText: String?
    val receivedAction = intent.action
    val receivedType = intent.type
    if (receivedAction == Intent.ACTION_SEND) {

        // check mime type
        if (receivedType!!.startsWith("text/")) {
            receivedText = intent
                .getStringExtra(Intent.EXTRA_TEXT)
            if (receivedText != null) {
                return receivedText
            }
        }
    } else if (receivedAction == Intent.ACTION_MAIN) {
        Log.e("shared@@", "onSharedIntent: nothing shared")
        return null
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun TabScreen(receiverText: String) {
    val tabs = listOf(
        TabItem.Home,
        TabItem.History,
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PinkColor
    ) {
        Column {
            // Create a TabRow with tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = PinkColor
            ) {
                tabs.forEachIndexed { index, tabItem ->
                    LeadingIconTab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tabItem.title,
                                style = if (selectedTabIndex == index) TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ) else TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabItem.icon,
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                        })
                }
            }

            // Display content based on the selected tab
            when (selectedTabIndex) {
                0 -> {
                    Log.d("intent@@", "!2onCreate: $receiverText")
                    HomeScreen(receiverText)
                }
//                1 -> BrowserScreen()
                1 -> {
                    HistoryScreen()
                }
            }
        }
    }
}

