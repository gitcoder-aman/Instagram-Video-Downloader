package com.tech.instasaver.screens

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.io.File

class FileListViewModel : ViewModel() {

    private var listFileVideo: List<File> = emptyList()
    private var listFilePicture: List<File> = emptyList()
    var listMedia: SnapshotStateList<File> = mutableStateListOf<File>()
    fun getMediaFile() {

        listMedia.clear()
        val targetPathForVideo: String =
            Environment.getExternalStorageDirectory().absolutePath + "/Movies/InstaSaver"
        val mediaDirectoryForVideo = File(targetPathForVideo)

        val targetPathForPicture: String =
            Environment.getExternalStorageDirectory().absolutePath + "/Pictures/InstaSaver"
        val mediaDirectoryForPicture = File(targetPathForPicture)

        listFileVideo = mediaDirectoryForVideo.listFiles()?.toList() ?: emptyList()
        listFilePicture = mediaDirectoryForPicture.listFiles()?.toList() ?: emptyList()

        listMedia.addAll(listFileVideo)
        listMedia.addAll(listFilePicture)
    }

    fun removeItem(mediaFile: File) {
        listMedia.remove(mediaFile)
        Log.d("@@delete", "HistoryScreen: ${listMedia.size}")
    }
}