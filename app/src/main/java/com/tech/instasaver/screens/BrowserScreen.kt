package com.tech.instasaver.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tech.instasaver.ui.theme.PinkColor

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen() {

    val url = "https://www.instagram.com/"
//    val url = "https://www.youtube.com/"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn {
                item {
                    WebViewBrowser(url)
                }
            }

        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBrowser(url : String) {

    var backEnabled by remember {
        mutableStateOf(false)
    }
    var isLoader = remember {
        mutableStateOf(true)
    }
    var webView : WebView ?= null

    if(isLoader.value){
        Dialog(onDismissRequest = {isLoader.value = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            CircularProgressIndicator(color = PinkColor)
        }
    }
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()

            //to enable JS
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            webChromeClient = WebChromeClient()
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.domStorageEnabled = true
            //to verify that the client requesting your web page is your actually your Android App.
            settings.userAgentString = System.getProperty("http.agent")

            webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    backEnabled = view?.canGoBack() == true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isLoader.value = false
                }
            }


            loadUrl(url)
            webView = this
        }
    }, update = {
        it.loadUrl(url)
        webView = it
    }, modifier = Modifier.fillMaxSize())


    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}
