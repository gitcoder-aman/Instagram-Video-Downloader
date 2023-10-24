package com.tech.instasaver.customcomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tech.instasaver.common.lato_bold
import com.tech.instasaver.common.lato_light
import com.tech.instasaver.common.lato_regular
import com.tech.instasaver.model.MultipleData
import com.tech.instasaver.screens.LoadImageWithGlide
import com.tech.instasaver.ui.theme.PinkColor
import com.tech.instasaver.viewmodel.DialogViewModel

@Composable
fun CustomDialog(
    viewModel: DialogViewModel,
    onDismiss: () -> Unit,
    onConfirm: (MultipleData) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }, properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(1.dp, color = PinkColor, shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Download Any Video",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W200,
                            fontFamily = lato_regular,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close",
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            onDismiss()
                        }
                    )
                }
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn {
                        items(viewModel.getDownloadLinkList()) { multipleData ->
                            EachRow(multipleData) { singleData ->
                                onConfirm(singleData)
                            }
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun EachRow(singleData: MultipleData, onClick: (MultipleData) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp, 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(
            1.dp,
            Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadImageWithGlide(singleData.link)

            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(0.3f)) {
                Text(
                    text = singleData.userName, style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = lato_bold,
                        color = Color.Black
                    )
                )
                //media Title
                Text(
                    text = singleData.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = lato_regular,
                        color = Color.Black,
                    ),
                    maxLines = 1
                )
            }
            val typeofMedia = if (singleData.link.contains(".mp4")) {
                "(Video)"
            } else {
                "(Image)"
            }
            Text(
                text = typeofMedia,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight.W200,
                    fontFamily = lato_light,
                    color = Color.Black,
                )
            )
            Icon(
                imageVector = Icons.Filled.Download,
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
                    .clickable {
                        onClick(singleData)
                    },
                tint = PinkColor
            )
        }
    }
}