package com.tech.instasaver.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tech.instasaver.viewmodel.DialogViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tech.instasaver.customcomponents.CustomDialog
import com.tech.instasaver.downloadProcess.startDownloadTask
import com.tech.instasaver.ui.theme.PinkColor

@Composable
fun DialogScreen(
    viewModel: DialogViewModel
) {
    var isStartDownload by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var progressValue by remember {
        mutableFloatStateOf(0f)
    }
    var link by remember {
        mutableStateOf("")
    }
    var storageDirectory by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    if (viewModel.isDialogShown) {
        CustomDialog(onDismiss = {
            viewModel.onDismissDialog()
        }, viewModel = viewModel) { singleData ->

            link = singleData.link
            storageDirectory = singleData.path
            isStartDownload = true
        }
    }
    if (isStartDownload) {
        LaunchedEffect(Unit) {
            startDownloadTask(link, storageDirectory) { progress ->
                progressValue = progress.toFloat()
                Log.d("@@Dialog", "DialogScreen: $progress")
                showDialog = true
            }
            showDialog = false
        }
        isStartDownload = false
    }
    // Show an AlertDialog when 'showDialog' is true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismissal if needed
                showDialog = false
            },
            title = { Text("Download") },
            text = { Text("Download is Progress, Please wait...") },
            confirmButton = {
                if (progressValue != 100f)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "${progressValue.toInt()}%")
                        Spacer(modifier = Modifier.width(4.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = PinkColor
                        )
                    }
                else {
                    Button(
                        onClick = {
                            if (progressValue == 100f)
                                showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Done")
                    }
                }
            }
        )
    }
}