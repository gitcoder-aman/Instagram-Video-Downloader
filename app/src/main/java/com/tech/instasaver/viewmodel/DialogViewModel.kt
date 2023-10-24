package com.tech.instasaver.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tech.instasaver.model.MultipleData

class DialogViewModel : ViewModel() {
    var isDialogShown by mutableStateOf(true)
        private set
    private var downloadLinkList = mutableListOf<MultipleData>()
    fun onDismissDialog(){
        isDialogShown = false
    }
    fun downloadLinkList(link: MutableList<MultipleData>){
        downloadLinkList = link
    }
    fun getDownloadLinkList() : List<MultipleData>{
        return downloadLinkList
    }
}