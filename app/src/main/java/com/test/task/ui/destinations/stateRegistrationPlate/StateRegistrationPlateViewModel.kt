package com.test.task.ui.destinations.stateRegistrationPlate

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.dataStores.AutoInfoDataStore
import com.test.task.dataStores.DriverLicenseNumberDataStore
import com.test.task.ui.utils.replaceLatinWithCyrillic
import com.test.task.ui.utils.showNotValidNumberToast
import com.test.task.ui.utils.validateNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateRegistrationPlateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val autoInfoDataStore: AutoInfoDataStore,
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : ViewModel() {

    var stateRegistrationPlate by mutableStateOf(TextFieldValue(text = ""))
        private set

    fun onStateRegistrationPlateChange(stateRegistrationPlate: TextFieldValue) {
        val text = replaceLatinWithCyrillic(stateRegistrationPlate.text.uppercase())

        this.stateRegistrationPlate = stateRegistrationPlate.copy(text = text)
    }

    var skipAlertDialogState by mutableStateOf(false)
        private set

    fun onSkipAlertDialogStateChange() {
        skipAlertDialogState = !skipAlertDialogState
    }

    var driverLicenseNumber by mutableStateOf<String?>(null)
        private set

    var continueState by mutableStateOf(false)
        private set

    fun continueHandler() {
        val isValid = validateNumbers(text = stateRegistrationPlate.text, formats = formats)

        if (isValid) {
            onAutoInfoUpdate()

            continueState = !continueState
        } else {
            showNotValidNumberToast(context = context)
        }
    }

    init {
        viewModelScope.launch {
            driverLicenseNumber = driverLicenseNumberDataStore.data.firstOrNull()
        }
    }

    private fun onAutoInfoUpdate() {
        viewModelScope.launch {
            autoInfoDataStore.updateData {
                it.copy(stateRegistrationPlate = stateRegistrationPlate.text.trim())
            }
        }
    }

    companion object {
        private const val validChars = "АВЕКМНОРСТУХ"

        private val formats = listOf(
            """^[$validChars]{1}\s*\d{3}\s*[$validChars]{2}\s*\d{2}$""", // Types 1, 1А
            """^[$validChars]{2}\s*\d{3}\s*\d{2}$""", // Type 1Б
            """^\d{4}\s*[$validChars]{2}\s*\d{2}$""", // Types 3, 4, 5, 7, 8
            """^[$validChars]{2}\s*\d{4}\s*\d{2}$""", // Types 4А, 2, 6
            """^[$validChars]{2}\s*\d{2}\s*[$validChars]{2}\s*\d{2}$""", // Type 4Б
            """^[$validChars]{1}\s*\d{4}\s*\d{2}$""", // Type 20
            """^\d{3}\s*[$validChars]{1}\s*\d{2}$""", // Type 21
            """^\d{4}\s*[$validChars]{1}\s*\d{2}$""" // Type 22
        )
    }
}