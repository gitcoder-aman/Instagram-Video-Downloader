package com.tech.instasaver.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HowToUseActivity

@Composable
fun InstaNavigation(navController: NavHostController) {


//    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Home) {
        composable(Home) {
//            HomeScreen()
        }
        composable(History) {
            HistoryScreen()
        }
//        composable(
//            route = "videoPlayer/{videoUri}",
//            arguments = listOf(navArgument("videoUri") {
//                type = NavType.StringType
//                defaultValue = "no string"
//            })
//        ) { backStackEntry ->
//            val videoUri = requireNotNull(backStackEntry.arguments).getString("videoUri")
//            if (videoUri != null) {
//                VideoPlayerScreen(videoUri, navController)
//            }
//        }

    }

}

const val Home = "home_screen"
const val History = "history_screen"
