package com.tech.instasaver.screens

import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FileListViewModel : ViewModel() {

    private var listMediaFile: List<File> = emptyList()
    var listMedia: SnapshotStateList<File> = mutableStateListOf<File>()
    fun getMediaFile() {


        listMedia.clear()
        val targetPath: String =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/InstaSaver/"
        val mediaDirectory = File(targetPath)
        listMediaFile = mediaDirectory.listFiles()?.toList() ?: emptyList()
        listMedia.addAll(listMediaFile)
    }

    fun removeItem(mediaFile: File) {
        listMedia.remove(mediaFile)
    }
}