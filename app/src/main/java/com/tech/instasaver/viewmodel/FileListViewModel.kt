package com.tech.instasaver.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class FileListViewModel : ViewModel() {

    private val _listFileVideo = MutableLiveData<List<File>>()
    val listFileVideo : LiveData<List<File>> = _listFileVideo
    private var targetPathForVideo : String = ""
    private var mediaDirectoryForVideo : File ?= null

    private val _listFileImage = MutableLiveData<List<File>>()
    val listFileImage : LiveData<List<File>> = _listFileImage
    private var targetPathForImage : String = ""
    private var mediaDirectoryForImage : File ?= null


    init {
        targetPathForVideo = Environment.getExternalStorageDirectory().absolutePath + "/Movies/InstaSaver"
        mediaDirectoryForVideo = File(targetPathForVideo)

        targetPathForImage = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/InstaSaver"
        mediaDirectoryForImage = File(targetPathForImage)
        refreshFileList()
    }

      fun refreshFileList(){

         mediaDirectoryForVideo?.takeIf { it.exists() }?.let { directory ->
             // Sort the video files by last modified timestamp in descending order
             val listFilesOfVideo = directory.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
             _listFileVideo.value = listFilesOfVideo
         }
          mediaDirectoryForImage?.takeIf { it.exists() }?.let { directory->
              // Sort the video files by last modified timestamp in descending order
              val listFilesOfImage = directory.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
              _listFileImage.value = listFilesOfImage
          }
    }
    fun removeFile(mediaFile: File, context: Context){
        if (mediaFile.exists()) {
            // Delete the file
            if (mediaFile.delete()) {
                Toast.makeText(
                    context,
                    "file is Deleted from Device.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d(
                    "@@delete",
                    "Failed to delete the file."
                )
            }

            // Refresh the list of video files after deletion
            refreshFileList()
        }else{
            Toast.makeText(
                context,
                "file not exist",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}