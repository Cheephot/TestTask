package com.test.task.application.formatters

object LatinWithCyrillicFormatter {
    fun replaceLatinWithCyrillic(input: String): String {
        return input.map { mapping[it] ?: it }.joinToString("")
    }

    private val mapping = mapOf(
        'A' to 'А',
        'B' to 'В',
        'C' to 'С',
        'E' to 'Е',
        'H' to 'Н',
        'K' to 'К',
        'M' to 'М',
        'O' to 'О',
        'P' to 'Р',
        'T' to 'Т',
        'X' to 'Х'
    )
}