package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.projach.comics_tracker.ui.composables.models.ComicsTestFieldUiModel

@Composable
fun UserForm(
    fields: List<ComicsTestFieldUiModel>,
    onSubmit: () -> Unit,
    error: String? = null
) {
    val focusManager = LocalFocusManager.current

    Column {
        fields.forEachIndexed { index, field ->
            val isLast = index == fields.lastIndex
            ComicsTextField(
                field,
                keyboardOptions = KeyboardOptions(
                    imeAction = if (isLast) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = {
                        focusManager.clearFocus()
                        onSubmit.invoke()
                    }
                ),
            )
            if (!isLast) {
                Spacer(Modifier.height(6.dp))
            }
        }

        Spacer(Modifier.height(4.dp))

        error?.let {
            Text(
                text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
        }
    }
}

