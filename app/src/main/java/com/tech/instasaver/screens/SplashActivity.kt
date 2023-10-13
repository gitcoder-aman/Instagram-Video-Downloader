package com.tech.instasaver.screens

import android.content.Intent
import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.tech.instasaver.MainActivity
import com.tech.instasaver.R
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.ui.theme.PinkColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    // Add your splash screen content here, such as logo, title, etc.
                    Text(
                        text = "Insta Saver", style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = FontFamily.Serif,
                            color = PinkColor
                        )
                    )
                    callMainActivity()
                }
            }
        }
    }

    private fun callMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)

        // Use a CoroutineScope to add a delay and then navigate to the main screen.
        lifecycleScope.launch {
            delay(2000) // Delay for 2 seconds (adjust as needed)
            startActivity(mainActivityIntent)
            finish() // Optional: Finish the splash screen activity to prevent going back to it.
        }
    }
}