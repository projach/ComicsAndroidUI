package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ExitDialog(
    description: String,
    exitText: String,
    stayText: String,
    onExitPress: () -> Unit,
    onStayPress: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Column {
            Text(
                description
            )
            Spacer(Modifier.height(30.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onExitPress
                ) {
                    Text(
                        exitText
                    )
                }
                Button(
                    onStayPress
                ) {
                    Text(
                        stayText
                    )
                }
            }
        }
    }
}