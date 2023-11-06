package com.test.task.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.spec.Route
import com.test.task.application.operations.queries.GetAutoInfoQuery
import com.test.task.application.operations.queries.GetAutoInfoQueryHandler
import com.test.task.application.operations.queries.GetDriverLicenseNumberQuery
import com.test.task.application.operations.queries.GetDriverLicenseNumberQueryHandler
import com.test.task.dataStores.models.AutoInfo
import com.test.task.ui.destinations.destinations.CertificateNumberScreenDestination
import com.test.task.ui.destinations.destinations.DriverLicenseNumberScreenDestination
import com.test.task.ui.destinations.destinations.ResultScreenDestination
import com.test.task.ui.destinations.destinations.StateRegistrationPlateScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestTaskApplicationViewModel @Inject constructor(
    private val getAutoInfoQueryHandler: GetAutoInfoQueryHandler,
    private val getDriverLicenseNumberQueryHandler: GetDriverLicenseNumberQueryHandler
) : ViewModel() {
    var startDestination by mutableStateOf<Route?>(null)
        private set

    private var autoInfo by mutableStateOf<AutoInfo?>(null)
    private var driverLicenseNumber by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            driverLicenseNumber = getDriverLicenseNumberQueryHandler
                .handle(GetDriverLicenseNumberQuery)
                .firstOrNull()

            autoInfo = getAutoInfoQueryHandler.handle(GetAutoInfoQuery).firstOrNull()

            determineNavigationDestination()
        }
    }

    private fun determineNavigationDestination() {
        startDestination = when {
            autoInfo?.stateRegistrationPlate.isNullOrEmpty() -> StateRegistrationPlateScreenDestination
            autoInfo?.certificateNumber.isNullOrEmpty() -> CertificateNumberScreenDestination
            driverLicenseNumber.isNullOrEmpty() -> DriverLicenseNumberScreenDestination
            else -> ResultScreenDestination
        }
    }
}