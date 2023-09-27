package com.tech.instasaver.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.VideoPlayerScreen

@Composable
fun InstaNavigation(navController: NavHostController) {


//    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {
        composable(Home){
//            HomeScreen()
        }

        composable(History) {
            HistoryScreen(navController)
        }
        composable(
            route = "videoPlayer/{videoUri}",
            arguments = listOf(navArgument("videoUri") {
                type = NavType.StringType
                defaultValue = "no string"
            })
        ) { backStackEntry ->
            val videoUri = requireNotNull(backStackEntry.arguments).getString("videoUri")
            if (videoUri != null) {
                VideoPlayerScreen(videoUri,navController)
            }
        }

    }

}

const val Home = "home_screen"
const val History = "history_screen"
const val VideoPlayer = "VideoPlayer_screen"