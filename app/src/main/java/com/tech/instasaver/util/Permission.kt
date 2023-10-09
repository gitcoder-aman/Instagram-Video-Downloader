package com.tech.instasaver.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tech.instasaver.MainActivity.Companion.PERMISSIONS_REQUEST_STORAGE

class Permission{

    fun checkStoragePermission(activity: Activity) : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val readMediaImage = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
            val readMediaVideo = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
            return readMediaImage == PackageManager.PERMISSION_GRANTED && readMediaVideo == PackageManager.PERMISSION_GRANTED
        }else {

            val readPermission = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val writePermission = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return readPermission == PackageManager.PERMISSION_GRANTED &&
                    writePermission == PackageManager.PERMISSION_GRANTED
        }
    }
    fun requestPermission(activity: Activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO),
                PERMISSIONS_REQUEST_STORAGE
            )
        }else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_STORAGE
            )
        }
    }
}