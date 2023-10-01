package com.tech.instasaver.common

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.P)
fun getClipBoardData(context: Context): String {
    val clipboardManager =
        ContextCompat.getSystemService(context, ClipboardManager::class.java)

    var urlText: String = ""

    if (clipboardManager?.hasPrimaryClip() == true) {
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            if(item.text.toString().contains("www.instagram.com/")){
                urlText = item.text.toString()
            }
        } else {
            Toast.makeText(context, "first copy link", Toast.LENGTH_SHORT).show()
        }
    }
    return urlText
}
@RequiresApi(Build.VERSION_CODES.P)
fun clearClipBoardData(context: Context){
    val clipboardManager =
        ContextCompat.getSystemService(context, ClipboardManager::class.java)

    clipboardManager?.clearPrimaryClip()
}