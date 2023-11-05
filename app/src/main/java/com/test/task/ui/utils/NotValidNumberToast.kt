package com.test.task.ui.utils

import android.content.Context
import android.widget.Toast
import com.foresko.debts.R

fun showNotValidNumberToast(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.number_entered_incorrectly),
        Toast.LENGTH_SHORT
    ).show()
}