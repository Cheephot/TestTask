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
import com.foresko.debts.ui.destinations.NavGraphs
import com.foresko.debts.ui.destinations.destinations.CertificateNumberScreenDestination
import com.foresko.debts.ui.destinations.destinations.DriverLicenseNumberScreenDestination
import com.foresko.debts.ui.destinations.destinations.ResultScreenDestination
import com.foresko.debts.ui.destinations.destinations.StateRegistrationPlateScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.Route

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

    val startDestination = determineStartDestination(
        shouldNavigateToStateRegistrationPlateScreen = viewModel.shouldNavigateToStateRegistrationPlateScreen(),
        shouldNavigateToCertificateNumber = viewModel.shouldNavigateToCertificateNumber(),
        shouldNavigateToDriverLicenseNumber = viewModel.shouldNavigateToDriverLicenseNumber(),
        shouldNavigateToResultScreen = viewModel.shouldNavigateToResultScreen()
    )

    val navController = rememberNavController()

    if (!viewModel.isInitializeAllVariables) return

    DestinationsNavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .statusBarsPadding(),
        navGraph = NavGraphs.root.copy(startRoute = startDestination),
        engine = rememberNavHostEngine(),
        navController = navController,
        dependenciesContainerBuilder = {
            dependency(RootNavigator(destinationsNavigator))
        }
    )
}

private fun determineStartDestination(
    shouldNavigateToStateRegistrationPlateScreen: Boolean,
    shouldNavigateToCertificateNumber: Boolean,
    shouldNavigateToDriverLicenseNumber: Boolean,
    shouldNavigateToResultScreen: Boolean,
): Route {
    return when {
        shouldNavigateToStateRegistrationPlateScreen -> StateRegistrationPlateScreenDestination
        shouldNavigateToCertificateNumber -> CertificateNumberScreenDestination
        shouldNavigateToDriverLicenseNumber -> DriverLicenseNumberScreenDestination
        shouldNavigateToResultScreen -> ResultScreenDestination
        else -> StateRegistrationPlateScreenDestination
    }
}