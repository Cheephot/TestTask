package com.test.task.ui.destinations.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.application.formatters.ResultFormatter
import com.test.task.application.operations.queries.GetAutoInfoQuery
import com.test.task.application.operations.queries.GetAutoInfoQueryHandler
import com.test.task.application.operations.queries.GetDriverLicenseNumberQuery
import com.test.task.application.operations.queries.GetDriverLicenseNumberQueryHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getAutoInfoQueryHandler: GetAutoInfoQueryHandler,
    private val getDriverLicenseNumberQueryHandler: GetDriverLicenseNumberQueryHandler
) : ViewModel() {
    var stateRegistrationPlate by mutableStateOf<String?>(null)
        private set

    var certificateNumber by mutableStateOf<String?>(null)
        private set

    var driverLicenseNumber by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            combine(
                getAutoInfoQueryHandler.handle(GetAutoInfoQuery),
                getDriverLicenseNumberQueryHandler.handle(GetDriverLicenseNumberQuery)
            ) { autoInfo, driverLicenseNumber ->
                autoInfo to driverLicenseNumber
            }.collectLatest {
                stateRegistrationPlate =
                    ResultFormatter.stateRegistrationPlateFormatter(it.first.stateRegistrationPlate)

                certificateNumber = ResultFormatter.numberFormatter(it.first.certificateNumber)

                driverLicenseNumber = ResultFormatter.numberFormatter(it.second)
            }
        }
    }
}