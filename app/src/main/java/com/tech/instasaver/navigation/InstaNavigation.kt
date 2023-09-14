package com.tech.instasaver.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tech.instasaver.screens.BrowserScreen
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen
import com.tech.instasaver.ui.theme.PinkColor

@Composable
fun InstaNavigation() {


    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home ){
        composable(Home){
            HomeScreen()
        }
        composable(Browser){
            BrowserScreen()
        }
        composable(History){
            HistoryScreen()
        }
    }

//    TabScreen(navController = navHostController)
}
const val Home = "home_screen"
const val Browser = "browser_screen"
const val History = "history_screen"