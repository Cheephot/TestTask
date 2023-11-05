package com.test.task.ui.utils

fun validateNumbers(text: String, formats: List<String>): Boolean {
    return formats.any { format -> text.trim().matches(Regex(format)) }
}