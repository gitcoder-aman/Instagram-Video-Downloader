package com.tech.instasaver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tech.instasaver.screens.BrowserScreen
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen

@Composable
fun InstaNavigation(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = home ){
        composable(home){
            HomeScreen()
        }
        composable(browser){
            BrowserScreen()
        }
        composable(history){
            HistoryScreen()
        }
    }
}
const val home = "home_screen"
const val browser = "browser_screen"
const val history = "history_screen"