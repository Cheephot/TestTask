package com.test.task.application.validators

import com.test.task.application.validators.core.Validator

class CertificateNumberValidator : Validator(formats = formats) {
    companion object {
        private const val validChars = "АБВЕЗКМНОРСТХУ"

        private val formats = listOf(
            """^\d{2}\s*[$validChars]{2}\s*\d{6}$""",
            """^\d{10}""",
        )
    }
}