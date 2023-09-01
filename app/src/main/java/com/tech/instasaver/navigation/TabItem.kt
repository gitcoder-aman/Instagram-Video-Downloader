package com.tech.instasaver.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tech.instasaver.screens.BrowserScreen
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen

//typealias ComposableFun = @Composable ()->Unit
sealed class TabItem(var icon: ImageVector, var title:String, var screen: @Composable ()->Unit){

    object  Home : TabItem(Icons.Outlined.Home,"Home",{ HomeScreen()})
    object  Browser : TabItem(Icons.Outlined.OpenInBrowser,"Browser",{ BrowserScreen() })
    object  History : TabItem(Icons.Outlined.History,"History",{ HistoryScreen() })
}
