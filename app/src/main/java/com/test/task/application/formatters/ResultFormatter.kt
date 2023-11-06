package com.test.task.application.formatters

object ResultFormatter {
    fun stateRegistrationPlateFormatter(text: String?): String? {
        return text?.replace(Regex("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)"), " ")
    }

    fun numberFormatter(text: String?): String? {
        return when {
            text?.matches(Regex("\\d{10}")) == true -> text.replaceFirst(
                Regex("(\\d{2})(\\d{2})(\\d{6})"),
                "$1 $2 $3"
            )

            text?.matches(Regex("\\d{2}[А-Я]{2}\\d{6}")) == true -> text.replaceFirst(
                Regex("(\\d{2})([А-Я]{2})(\\d{6})"),
                "$1 $2 $3"
            )

            else -> text
        }
    }
}