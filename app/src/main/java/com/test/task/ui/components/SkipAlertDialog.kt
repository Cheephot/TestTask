package com.test.task.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.test.task.R


@Composable
fun SkipAlertDialog(
    @StringRes text: Int,
    skipAlertDialogState: Boolean,
    onSkipAlertDialogStateChange: () -> Unit,
    navigateTo: () -> Unit
) {
    if (skipAlertDialogState) {
        AlertDialog(
            onDismissRequest = onSkipAlertDialogStateChange,
            confirmButton = {
                TextButton(onClick = navigateTo) {
                    Text(
                        text = stringResource(R.string.skip)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onSkipAlertDialogStateChange) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(text)
                )
            }
        )
    }
}