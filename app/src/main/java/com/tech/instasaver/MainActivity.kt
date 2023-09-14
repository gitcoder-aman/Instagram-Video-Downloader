package com.tech.instasaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tech.instasaver.navigation.Browser
import com.tech.instasaver.navigation.History
import com.tech.instasaver.navigation.Home
import com.tech.instasaver.navigation.InstaNavigation
import com.tech.instasaver.navigation.TabItem
import com.tech.instasaver.screens.BrowserScreen
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen
import com.tech.instasaver.ui.theme.InstaSaverTheme
import com.tech.instasaver.ui.theme.PinkColor
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.Text as Text


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaSaverTheme {


                Scaffold(topBar = { TopBar() }) { padding ->
                    Column(
                        modifier = Modifier.padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        InstaNavigation()
//                        HomeScreen(navHostController = navController)
                        TabScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name), fontSize = 18.sp) },
        backgroundColor = PinkColor,
        contentColor = Color.White, elevation = 0.dp
    )
}

@Composable
fun TabScreen() {
    val tabs = listOf(
        TabItem.Home,
        TabItem.Browser,
        TabItem.History,
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PinkColor
    ) {
        Column {
            // Create a TabRow with tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = PinkColor
            ) {
                tabs.forEachIndexed { index, tabItem ->
                    LeadingIconTab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tabItem.title,
                                style = if (selectedTabIndex == index) TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ) else TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabItem.icon,
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                        })
                }
            }

            // Display content based on the selected tab
            when (selectedTabIndex) {
                0 -> HomeScreen()
                1 -> BrowserScreen()
                2 -> HistoryScreen()
            }
        }
    }
}

