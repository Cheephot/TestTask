package com.test.task.ui.destinations.driverLicenseNumber

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.application.operations.commands.UpdateDriverLicenseNumberCommand
import com.test.task.application.operations.commands.UpdateDriverLicenseNumberCommandHandler
import com.test.task.application.validators.DriverLicenseNumberValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverLicenseNumberViewModel @Inject constructor(
    private val updateDriverLicenseNumberCommandHandler: UpdateDriverLicenseNumberCommandHandler
) : ViewModel() {

    fun validateDriverLicenseNumber(input: String): Boolean {
        return DriverLicenseNumberValidator().isValid(input)
    }

    fun onDriverLicenseNumberUpdate(driverLicenseNumber: String) {
        viewModelScope.launch {
            updateDriverLicenseNumberCommandHandler.handle(
                UpdateDriverLicenseNumberCommand(
                    driverLicenseNumber = driverLicenseNumber
                )
            )
        }
    }
}