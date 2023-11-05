package com.test.task.ui.destinations.driverLicenseNumber

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.task.dataStores.DriverLicenseNumberDataStore
import com.test.task.ui.utils.replaceLatinWithCyrillic
import com.test.task.ui.utils.showNotValidNumberToast
import com.test.task.ui.utils.validateNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverLicenseNumberViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : ViewModel() {
    var driverLicenseNumber by mutableStateOf(TextFieldValue(text = ""))
        private set

    fun onDriverLicenseNumberChange(driverLicenseNumber: TextFieldValue) {
        val text = replaceLatinWithCyrillic(driverLicenseNumber.text.uppercase())

        this.driverLicenseNumber = driverLicenseNumber.copy(text = text)
    }

    var skipAlertDialogState by mutableStateOf(false)
        private set

    fun onSkipAlertDialogStateChange() {
        skipAlertDialogState = !skipAlertDialogState
    }

    var continueState by mutableStateOf(false)
        private set

    fun continueHandler() {
        val isValid = validateNumbers(text = driverLicenseNumber.text, formats = formats)

        if (isValid) {
            onDriverLicenseNumberUpdate()

            continueState = !continueState
        } else {
            showNotValidNumberToast(context = context)
        }
    }

    private fun onDriverLicenseNumberUpdate() {
        viewModelScope.launch {
            driverLicenseNumberDataStore.updateData {
                driverLicenseNumber.text.trim()
            }
        }
    }

    companion object {
        private const val validChars = "АБВЕКМНОРСТХУЧ"

        private val formats = listOf(
            """^\d{2}\s*[$validChars]{2}\s*\d{6}$""",
            """^\d{10}""",
        )
    }
}