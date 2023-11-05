package com.test.task.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.dataStores.AutoInfo
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.DriverLicenseNumberDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestTaskApplicationViewModel @Inject constructor(
    private val autoInfoDataStore: AutoInfoDataStore,
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : ViewModel() {
    private var autoInfo by mutableStateOf<AutoInfo?>(null)

    private var driverLicenseNumber by mutableStateOf<String?>(null)

    var isInitializeAllVariables by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            driverLicenseNumber = driverLicenseNumberDataStore.data.firstOrNull()

            autoInfo = autoInfoDataStore.data.firstOrNull()

            isInitializeAllVariables = true
        }
    }

    private val certificateNumberIsEmpty: Boolean
        get() = autoInfo?.certificateNumber.isNullOrEmpty()

    private val stateRegistrationPlateIsEmpty: Boolean
        get() = autoInfo?.stateRegistrationPlate.isNullOrEmpty()

    private val driverLicenseNumberIsEmpty: Boolean
        get() = driverLicenseNumber.isNullOrEmpty()

    fun shouldNavigateToCertificateNumber() =
        !stateRegistrationPlateIsEmpty && certificateNumberIsEmpty

    fun shouldNavigateToDriverLicenseNumber() =
        !stateRegistrationPlateIsEmpty && driverLicenseNumberIsEmpty

    fun shouldNavigateToResultScreen() = !certificateNumberIsEmpty && !driverLicenseNumberIsEmpty

    fun shouldNavigateToStateRegistrationPlateScreen() = stateRegistrationPlateIsEmpty
}