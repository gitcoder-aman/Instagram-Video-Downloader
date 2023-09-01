package com.tech.instasaver

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.TabRow
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.tech.instasaver.apifetch_data.data.model.Variable
import com.tech.instasaver.apifetch_data.util.ApiState
import com.tech.instasaver.apifetch_data.viewModels.MainViewModel
import com.tech.instasaver.navigation.TabItem
import com.tech.instasaver.screens.BrowserScreen
import com.tech.instasaver.screens.HistoryScreen
import com.tech.instasaver.screens.HomeScreen
import com.tech.instasaver.ui.theme.InstaSaverTheme
import com.tech.instasaver.ui.theme.Pink40
import com.tech.instasaver.ui.theme.PinkColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.material3.Text as Text


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaSaverTheme {
//                val navHostController = rememberNavController()

//                InstaNavigation(navHostController)

                val reelId by remember {
                    mutableStateOf("CvJLB8GOnko")
                }
                Variable.reelId = reelId
                Log.d("@@@@main", "onCreate: ${Variable.reelId}")

                GETData(mainViewModel = mainViewModel)
            }
        }
    }
    @Composable
    fun GETData(mainViewModel: MainViewModel){
        when(val result = mainViewModel.response.value){
            is ApiState.Success->{
                Log.d("@@@@main", "GETData: ${result.data.body()?.graphql?.shortcode_media?.video_url}")
                MainScreen()
            }
            is ApiState.Failure->{
                Log.d("@@@@main", "GETData: ${result.msg}")
            }
            is ApiState.Loading->{
                CircularProgressIndicator()
            }
            is ApiState.Empty->{
                Toast.makeText(applicationContext, "Video Url is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val tabs = listOf(
        TabItem.Home,
        TabItem.Browser,
        TabItem.History,
    )

    val pagerState = rememberPagerState()


    Scaffold(topBar = { TopBar() }) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = PinkColor,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(
                    pagerState, tabPositions
                ), color = Pink40
            )
        }) {
        tabs.forEachIndexed { index, tabItem ->
            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = tabItem.title,
                        style = if (pagerState.currentPage == index) TextStyle(
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
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        count = tabs.size,
        state = pagerState
    ) { page ->
//        Log.d("@@@@", "TabsContent: ${page} ${tabs[page].screen}")
//        tabs[page].screen
        if (page == 0) {
            HomeScreen()
        } else if (page == 1) {
            BrowserScreen()
        } else {
            HistoryScreen()
        }
    }
}
