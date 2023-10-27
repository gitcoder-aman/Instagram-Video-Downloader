package com.tech.instasaver.downloadProcess

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlin.coroutines.CoroutineContext

fun startDownload(context: Context, downloadUrl: String, storageDirectory: String) {

    val request = DownloadManager.Request(Uri.parse(downloadUrl))
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
    request.setTitle("Downloading File")
    request.setDescription("Download in progress")
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"insta3181435705570019321")

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}