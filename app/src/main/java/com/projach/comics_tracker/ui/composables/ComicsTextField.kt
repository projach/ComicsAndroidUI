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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel
import com.projach.comics_tracker.ui.composables.models.FieldType

@Composable
fun ComicsTextField(
    comicsTestFieldUiModel: ComicsTestFieldUiModel,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
){
    val isPassword = comicsTestFieldUiModel.fieldType == FieldType.Password

    Column {
        comicsTestFieldUiModel.label?.let {
            Text(it)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = comicsTestFieldUiModel.text,
            onValueChange = { comicsTestFieldUiModel.onValueChanged.invoke(it) },
            placeholder = { comicsTestFieldUiModel.hint?.let { Text(it) } },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            isError = comicsTestFieldUiModel.error != null,
            supportingText = {
                if (comicsTestFieldUiModel.error != null){
                    Text(comicsTestFieldUiModel.error)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}