package com.tech.instasaver.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.instasaver.R
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.ui.theme.PinkColor

@Composable
fun HomeScreen() {

    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
//            if (videoResponse.isLoading) {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator(color = PinkColor)
//                }
//            }
//            if (videoResponse.data?.url != null) {
//                Log.d("@@@@", videoResponse.data.url)
//            }

            TextFieldLayout(text = text, onValueChange = {
                text = it
            })

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(stringResource(id = R.string.paste_link))
                Button(stringResource(R.string.download))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldLayout(text: String, onValueChange: (String) -> Unit) {
    TextField(value = text, onValueChange = onValueChange, modifier = Modifier
        .padding(15.dp)
        .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
        .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(fontFamily = lato_regular),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White,
            cursorColor = PinkColor,
            focusedTrailingIconColor = Color.Black,
            selectionColors = TextSelectionColors(
                handleColor = PinkColor,
                backgroundColor = PinkColor
            )
        ), keyboardActions = KeyboardActions(KeyboardActions.Default.onSearch), placeholder = {
            Text(
                text = stringResource(R.string.paste_instagram_link_here), style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W300,
                    fontFamily = lato_regular
                )
            )
        }
    )
}

@Composable
fun Button(buttonText: String) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = PinkColor,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Text(
            text = buttonText, style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.W300,
                fontFamily = lato_regular
            )
        )
    }
}