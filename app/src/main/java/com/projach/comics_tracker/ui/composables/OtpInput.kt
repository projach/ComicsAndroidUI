package com.projach.comics_tracker.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OtpInput(
    modifier: Modifier = Modifier,
    otpCode: String,
    onOtpCodeChange: (String) -> Unit,
    otpLength: Int = 6,
    error: String?,
    onOtpComplete: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = otpCode,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(otpLength)
                val wasIncomplete = otpCode.length < otpLength
                onOtpCodeChange(digits)
                if (digits.length == otpLength && wasIncomplete) {
                    onOtpComplete()
                }
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(otpLength) { index ->
                        val char = otpCode.getOrNull(index)?.toString().orEmpty()
                        val isActive =
                            isFocused && index == otpCode.length.coerceAtMost(otpLength - 1)
                        OtpBox(char = char, isActive = isActive, isError = error != null)
                    }
                }
            },
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun OtpBox(char: String, isActive: Boolean, isError: Boolean) {
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isActive -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Surface(
        modifier = Modifier.size(width = 48.dp, height = 56.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = borderColor
        ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = char, style = MaterialTheme.typography.headlineSmall)
        }
    }
}