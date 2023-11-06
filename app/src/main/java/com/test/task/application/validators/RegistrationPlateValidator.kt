package com.test.task.application.validators

import com.test.task.application.validators.core.Validator

class RegistrationPlateValidator : Validator(formats = formats) {
    companion object {
        private const val validChars = "АВЕКМНОРСТУХ"

        private val formats = listOf(
            """^[$validChars]{1}\s*\d{3}\s*[$validChars]{2}\s*\d{2}$""", // Types 1, 1А
            """^[$validChars]{2}\s*\d{3}\s*\d{2}$""", // Type 1Б
            """^\d{4}\s*[$validChars]{2}\s*\d{2}$""", // Types 3, 4, 5, 7, 8
            """^[$validChars]{2}\s*\d{4}\s*\d{2}$""", // Types 4А, 2, 6
            """^[$validChars]{2}\s*\d{2}\s*[$validChars]{2}\s*\d{2}$""", // Type 4Б
            """^[$validChars]{1}\s*\d{4}\s*\d{2}$""", // Type 20
            """^\d{3}\s*[$validChars]{1}\s*\d{2}$""", // Type 21
            """^\d{4}\s*[$validChars]{1}\s*\d{2}$""" // Type 22
        )
    }
}