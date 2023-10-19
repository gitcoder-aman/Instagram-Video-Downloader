package com.tech.instasaver.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogShow(alertDialogShowWhenSwitch: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {
                      alertDialogShowWhenSwitch.value = false
        },
        title = {
            Row {
                Icon(Icons.Default.WarningAmber, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Please Wait")
            }
        },
        text = {
            Text(text = "Wait some time while downloading in progress.")
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        alertDialogShowWhenSwitch.value = false
                    }
                ) {
                    Text("OK", color = Color.Red)
                }
            }
        }
    )
}