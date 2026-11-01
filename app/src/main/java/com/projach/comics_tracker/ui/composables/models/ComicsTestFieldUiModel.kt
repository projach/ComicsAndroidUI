package com.projach.comics_tracker.ui.composables.models

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

data class ComicsTestFieldUiModel(
    val text: String = "",
    val label: String?,
    val hint: String?,
    val onTextChanged: (String) -> Unit
)
