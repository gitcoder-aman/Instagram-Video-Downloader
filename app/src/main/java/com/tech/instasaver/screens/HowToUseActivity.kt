package com.tech.instasaver.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.instasaver.R
import com.tech.instasaver.ui.theme.InstaSaverTheme
import com.tech.instasaver.ui.theme.PinkColor

class HowToUseActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaSaverTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = getString(R.string.how_to_download),
                                color = Color.Black,
                                fontSize = 20.sp,
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onBackPressed()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = PinkColor,
                            navigationIconContentColor = Color.Black,
                            titleContentColor = Color.Gray
                        )
                    )
                }) { padding ->

                    val scrollState = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize(), contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Spacer(modifier = Modifier.height(4.dp))

                            ImageEach(
                                titleNumber = 1,
                                title = getString(R.string.open_instagram),
                                direction = R.drawable.direction1
                            )
                            ImageEach(
                                titleNumber = 2,
                                title = getString(R.string.select_app),
                                direction = R.drawable.direction2
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = getString(R.string.or),
                                color = Color.Gray,
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            ImageEach(
                                titleNumber = 1,
                                title = getString(R.string.open_instagram),
                                direction = R.drawable.direction3
                            )
                            ImageEach(
                                titleNumber = 2,
                                title = getString(R.string.launch_app),
                                direction = R.drawable.direction4
                            )

                        }
                    }
                    Modifier.padding(padding)
                }
            }
        }

    }
}

@Composable
fun ImageEach(titleNumber: Int, title: String, direction: Int) {
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularText(titleNumber)

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title, style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W300,
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = direction),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CircularText(titleNumber: Int) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(
                color = Color.Red
            )
            .clip(CircleShape)
    ) {
        Text(
            text = titleNumber.toString(),
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}