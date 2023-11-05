package com.test.task.ui.destinations.certificateNumber

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
class CertificateNumberViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val autoInfoDataStore: AutoInfoDataStore,
    private val driverLicenseNumberDataStore: DriverLicenseNumberDataStore
) : ViewModel() {
    var certificateNumber by mutableStateOf(TextFieldValue(text = ""))
        private set

    fun onCertificateNumberChange(certificateNumber: TextFieldValue) {
        val text = replaceLatinWithCyrillic(certificateNumber.text.uppercase())

        this.certificateNumber = certificateNumber.copy(text = text)
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
        val isValid = validateNumbers(text = certificateNumber.text, formats = formats)

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
                it.copy(certificateNumber = certificateNumber.text.trim())
            }
        }
    }

    companion object {
        private const val validChars = "АБВЕЗКМНОРСТХУ"

        private val formats = listOf(
            """^\d{2}\s*[$validChars]{2}\s*\d{6}$""",
            """^\d{10}""",
        )
    }
}