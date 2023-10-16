package com.tech.instasaver.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun getClipBoardData(context: Context): String {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    var urlText: String = ""
    val clipData = clipboardManager.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        val item = clipData.getItemAt(0)
        if (item.text.toString().contains("www.instagram.com/")) {
            urlText = item.text.toString()
        }
    } else {
        Toast.makeText(context, "first copy link", Toast.LENGTH_SHORT).show()
    }
    return urlText
}

fun clearClipBoardData(context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("", "")
    clipboard.setPrimaryClip(clipData)
}