package com.test.task.application.validators.core

import com.test.task.application.formatters.LatinWithCyrillicFormatter

abstract class Validator(private val formats: List<String>) {
    open fun isValid(input: String): Boolean {
        val formattedInputText = LatinWithCyrillicFormatter.replaceLatinWithCyrillic(input)

        return validateNumbers(formattedInputText)
    }

    private fun validateNumbers(text: String): Boolean {
        return formats.any { format -> text.trim().matches(Regex(format)) }
    }
}