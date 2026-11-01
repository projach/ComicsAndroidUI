package com.projach.comics_tracker.ui.composables.models

enum class FieldType { Text, Password, Email }
data class ComicsTestFieldUiModel(
    val text: String = "",
    val label: String?,
    val hint: String?,
    val error: String? = null,
    val fieldType: FieldType = FieldType.Text,
    val onValueChanged: (String) -> Unit
)
