package com.test.task.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.task.R

@Composable
fun DefaultEnterNumbersUi(
    @StringRes text: Int,
    onContinueClick: () -> Unit,
    onSkipAlertDialogStateChange: () -> Unit,
    textField: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(text),
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        textField()

        Spacer(modifier = Modifier.height(16.dp))

        NextPageButtons(
            onContinueClick = onContinueClick,
            onSkipAlertDialogStateChange = onSkipAlertDialogStateChange
        )
    }
}

@Composable
private fun NextPageButtons(
    onContinueClick: () -> Unit,
    onSkipAlertDialogStateChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        NextPageButton(
            name = R.string.skip,
            onClick = onSkipAlertDialogStateChange
        )

        Spacer(modifier = Modifier.weight(1f))

        NextPageButton(
            name = R.string.continueText,
            onClick = onContinueClick
        )
    }
}

@Composable
private fun NextPageButton(
    @StringRes name: Int,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = stringResource(name),
            color = Color.Blue,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}