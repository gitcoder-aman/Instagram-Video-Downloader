package com.tech.instasaver.downloadFile

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(DelicateCoroutinesApi::class)
suspend fun startDownloadTask(
    uriLink: String,
    storageDirectory: String,
    context: Context,
    progressCallback: (Int) -> Unit

) {

    Toast.makeText(context, "Download in Background Please wait.", Toast.LENGTH_LONG).show()

    GlobalScope.launch(Dispatchers.IO) {

        try {

            val url = URL(uriLink)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept-Encoding", "identity")
            connection.connectTimeout = 5000 // 5 seconds
            connection.readTimeout = 10000   // 10 seconds
            connection.instanceFollowRedirects = true
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
        }catch (e : Exception){
            Toast.makeText(context, "Something went wrong.Try Again!", Toast.LENGTH_SHORT).show()
            Log.d("@@downloadFile", "startDownloadTask: ${e.message}")
        }
    }
}