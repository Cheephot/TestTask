package com.test.task.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.test.task.ui.destinations.NavGraphs

@Composable
fun TestTaskApplicationUi(
    viewModel: TestTaskApplicationViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val navController = rememberNavController()

    if (viewModel.startDestination != null) {
        DestinationsNavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .navigationBarsPadding()
                .statusBarsPadding(),
            navGraph = NavGraphs.root.copy(startRoute = viewModel.startDestination!!),
            engine = rememberNavHostEngine(),
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(RootNavigator(destinationsNavigator))
            }
        )
    }
}