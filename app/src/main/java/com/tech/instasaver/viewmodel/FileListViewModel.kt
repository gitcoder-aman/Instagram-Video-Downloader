package com.tech.instasaver.viewmodel

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.Arrays

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

        // Sort the video files by last modified timestamp in descending order
        listFileVideo = mediaDirectoryForVideo.listFiles()?.sortedWith(compareByDescending { it.lastModified() }) ?: emptyList()

        // Sort the picture files by last modified timestamp in descending order
        listFilePicture = mediaDirectoryForPicture.listFiles()?.sortedWith(compareByDescending { it.lastModified() }) ?: emptyList()


        listMedia.addAll(listFileVideo)
        listMedia.addAll(listFilePicture)

    }

    fun removeItem(mediaFile: File) {
        listMedia.remove(mediaFile)
        Log.d("@@delete", "HistoryScreen: ${listMedia.size}")
    }
}