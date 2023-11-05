package com.test.task.ui.destinations.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.DriverLicenseNumberDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val autoInfoDataStore: AutoInfoDataStore,
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : ViewModel() {
    var stateRegistrationPlate by mutableStateOf<String?>(null)
        private set

    var certificateNumber by mutableStateOf<String?>(null)
        private set

    var driverLicenseNumber by mutableStateOf<String?>(null)
        private set

    var isInitializeAllVariables by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            combine(
                autoInfoDataStore.data,
                driverLicenseNumberDataStore.data
            ) { autoInfo, driverLicenseNumber ->
                autoInfo to driverLicenseNumber
            }.collectLatest {
                stateRegistrationPlate =
                    stateRegistrationPlateFormatter(it.first.stateRegistrationPlate)

                certificateNumber = numberFormatter(it.first.certificateNumber)

                driverLicenseNumber = numberFormatter(it.second)

                isInitializeAllVariables = true
            }
        }
    }

    private fun stateRegistrationPlateFormatter(text: String?): String? {
        return text?.replace(Regex("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)"), " ")
    }

    private fun numberFormatter(text: String?): String? {
        return when {
            text?.matches(Regex("\\d{10}")) == true -> text.replaceFirst(
                Regex("(\\d{2})(\\d{2})(\\d{6})"),
                "$1 $2 $3"
            )

            text?.matches(Regex("\\d{2}[А-Я]{2}\\d{6}")) == true -> text.replaceFirst(
                Regex("(\\d{2})([А-Я]{2})(\\d{6})"),
                "$1 $2 $3"
            )

            else -> text
        }
    }
}