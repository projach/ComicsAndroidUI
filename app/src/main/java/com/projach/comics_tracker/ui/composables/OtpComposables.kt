package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projach.comics_tracker.ui.theme.ErrorColor

@Composable
fun OtpInputField(
    onCodeComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    otpLength: Int = 6,
    error: String?
) {
    var textFieldValue by remember(value) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val digitsOnly = newValue.text.filter { it.isDigit() }.take(otpLength)

            textFieldValue = TextFieldValue(
                text = digitsOnly,
                selection = TextRange(digitsOnly.length)
            )
            onValueChange(digitsOnly)

            if (digitsOnly.length == otpLength) {
                onCodeComplete.invoke(digitsOnly)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Box {
                Box(modifier = Modifier.size(0.dp)) {
                    innerTextField()
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(otpLength) { index ->
                            val char = value.getOrNull(index)?.toString() ?: ""
                            val isFocusedBox = index == value.length

                            OtpBox(
                                number = char,
                                isFocused = isFocusedBox
                            )
                        }
                    }
                    error?.let {
                        Spacer(Modifier.size(26.dp))
                        Text(it, color = ErrorColor)
                    }
                }
            }
        }
    )
}

@Composable
fun OtpBox(
    number: String,
    isFocused: Boolean
) {
    val borderColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    Box(
        Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}