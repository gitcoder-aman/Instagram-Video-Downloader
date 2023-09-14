package com.tech.instasaver.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

typealias ComposableFun = @Composable ()->Unit
sealed class TabItem(var icon: ImageVector, var title:String){

    object  Home : TabItem(Icons.Outlined.Home,"Home")
    object  Browser : TabItem(Icons.Outlined.OpenInBrowser,"Browser")
    object  History : TabItem(Icons.Outlined.History,"History")
}
