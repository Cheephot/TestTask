package com.test.task.ui.destinations.certificateNumber

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.test.task.R
import com.test.task.ui.RootNavGraph
import com.test.task.ui.RootNavigator
import com.test.task.ui.components.DefaultEnterNumbersUi
import com.test.task.ui.components.SkipAlertDialog
import com.test.task.ui.destinations.destinations.DriverLicenseNumberScreenDestination
import com.test.task.ui.destinations.destinations.ResultScreenDestination
import com.test.task.ui.utils.showNotValidNumberToast

@Composable
@Destination
@RootNavGraph
fun CertificateNumberScreen(
    viewModel: CertificateNumberViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    CertificateNumberScreen(
        viewModel = viewModel,
        navigateToResultScreen = {
            rootNavigator.navigate(ResultScreenDestination)
        },
        navigateToDriverLicenseNumberScreen = {
            rootNavigator.navigate(
                DriverLicenseNumberScreenDestination
            )
        }
    )
}

@Composable
private fun CertificateNumberScreen(
    viewModel: CertificateNumberViewModel,
    navigateToResultScreen: () -> Unit,
    navigateToDriverLicenseNumberScreen: () -> Unit
) {
    val context = LocalContext.current

    val (skipAlertDialogState, onSkipAlertDialogStateChange) = remember { mutableStateOf(false) }

    val (certificateNumber, onCertificateNumberChange) = remember {
        mutableStateOf(TextFieldValue(""))
    }

    SkipAlertDialog(
        text = R.string.skip_certificate_description,
        skipAlertDialogState = skipAlertDialogState,
        onSkipAlertDialogStateChange = { onSkipAlertDialogStateChange(!skipAlertDialogState) },
        navigateTo = {
            if (viewModel.driverLicenseNumber.isNullOrEmpty()) {
                navigateToDriverLicenseNumberScreen()
            } else {
                navigateToResultScreen()
            }
        }
    )

    DefaultEnterNumbersUi(
        text = R.string.enter_sts,
        onContinueClick = {
            if (viewModel.validateCertificateNumber(certificateNumber.text)) {
                viewModel.onAutoInfoUpdate(certificateNumber.text)

                if (viewModel.driverLicenseNumber.isNullOrEmpty()) {
                    navigateToDriverLicenseNumberScreen()
                } else {
                    navigateToResultScreen()
                }
            } else {
                showNotValidNumberToast(context = context)
            }
        },
        onSkipAlertDialogStateChange = { onSkipAlertDialogStateChange(!skipAlertDialogState) }
    ) {
        CertificateTextField(
            certificateNumber = certificateNumber,
            onCertificateNumberChange = onCertificateNumberChange
        )
    }
}

@Composable
private fun CertificateTextField(
    certificateNumber: TextFieldValue,
    onCertificateNumberChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = certificateNumber.copy(text = certificateNumber.text.uppercase()),
        onValueChange = onCertificateNumberChange,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        visualTransformation = certificateTransformation()
    )
}

private fun certificateTransformation() = VisualTransformation { text ->
    val offsets = MutableList(text.length + 1) { -1 }

    val transformedText = buildString {
        var charCount = 0
        var transformedIndex = 0

        for (index in text.indices) {
            val char = text[index]

            if (char == ' ') continue

            if ((charCount == 2 && this.length == 2) || (charCount == 2 && this.length == 5)) {
                append(' ')
                transformedIndex++
                charCount = 0
            }

            append(char)
            offsets[index] = transformedIndex
            transformedIndex++
            charCount++
        }
        offsets[text.length] = length
    }

    val mapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offsets[offset].coerceAtMost(transformedText.length)
        }

        override fun transformedToOriginal(offset: Int): Int {
            for (i in offsets.indices.reversed()) {
                if (offsets[i] <= offset) {
                    return i
                }
            }
            return text.length
        }
    }

    TransformedText(AnnotatedString(transformedText), mapping)
}



