package com.test.task.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ContinueLaunch(
    continueState: Boolean,
    navigateTo: () -> Unit
) {
    LaunchedEffect(key1 = continueState) {
        if (continueState) navigateTo()
    }
}