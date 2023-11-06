package com.test.task.ui.destinations.certificateNumber

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.application.operations.commands.UpdateAutoInfoCommand
import com.test.task.application.operations.commands.UpdateAutoInfoCommandHandler
import com.test.task.application.operations.queries.GetAutoInfoQuery
import com.test.task.application.operations.queries.GetAutoInfoQueryHandler
import com.test.task.application.operations.queries.GetDriverLicenseNumberQuery
import com.test.task.application.operations.queries.GetDriverLicenseNumberQueryHandler
import com.test.task.application.validators.CertificateNumberValidator
import com.test.task.dataStores.models.AutoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CertificateNumberViewModel @Inject constructor(
    private val updateAutoInfoCommandHandler: UpdateAutoInfoCommandHandler,
    private val getAutoInfoQueryHandler: GetAutoInfoQueryHandler,
    private val getDriverLicenseNumberQueryHandler: GetDriverLicenseNumberQueryHandler
) : ViewModel() {
    var driverLicenseNumber by mutableStateOf<String?>(null)
        private set

    fun validateCertificateNumber(input: String): Boolean {
        return CertificateNumberValidator().isValid(input)
    }

    fun onAutoInfoUpdate(certificateNumber: String) {
        viewModelScope.launch {
            updateAutoInfoCommandHandler.handle(
                UpdateAutoInfoCommand(
                    autoInfo.value?.copy(certificateNumber = certificateNumber) ?: AutoInfo()
                )
            )
        }
    }

    private val autoInfo = MutableStateFlow<AutoInfo?>(null)

    init {
        viewModelScope.launch {
            viewModelScope.launch {
                driverLicenseNumber = getDriverLicenseNumberQueryHandler
                    .handle(GetDriverLicenseNumberQuery)
                    .firstOrNull()

                autoInfo.value = getAutoInfoQueryHandler.handle(GetAutoInfoQuery).firstOrNull()
            }
        }
    }
}