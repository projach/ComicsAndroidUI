package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Composable
fun ComicsTextField(
    comicsTestFieldUiModel: ComicsTestFieldUiModel,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
){
    Column() {
        comicsTestFieldUiModel.label?.let {
            Text(it)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = comicsTestFieldUiModel.text,
            onValueChange = { comicsTestFieldUiModel.onTextChanged.invoke(it) },
            placeholder = { comicsTestFieldUiModel.hint?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}