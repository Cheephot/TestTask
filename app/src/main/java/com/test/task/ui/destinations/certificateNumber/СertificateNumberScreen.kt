package com.test.task.ui.destinations.certificateNumber

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.debts.R
import com.foresko.debts.ui.destinations.destinations.DriverLicenseNumberScreenDestination
import com.foresko.debts.ui.destinations.destinations.ResultScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.test.task.ui.RootNavGraph
import com.test.task.ui.RootNavigator
import com.test.task.ui.components.ContinueLaunch
import com.test.task.ui.components.DefaultEnterNumbersUi
import com.test.task.ui.components.SkipAlertDialog

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
    ContinueLaunch(continueState = viewModel.continueState) {
        if (viewModel.driverLicenseNumber.isNullOrEmpty()) {
            navigateToDriverLicenseNumberScreen()
        } else {
            navigateToResultScreen()
        }
    }

    SkipAlertDialog(
        text = R.string.skip_certificate_description,
        skipAlertDialogState = viewModel.skipAlertDialogState,
        onSkipAlertDialogStateChange = viewModel::onSkipAlertDialogStateChange,
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
        continueHandler = viewModel::continueHandler,
        onSkipAlertDialogStateChange = viewModel::onSkipAlertDialogStateChange
    ) {
        CertificateTextField(
            certificateNumber = viewModel.certificateNumber,
            onCertificateNumberChange = viewModel::onCertificateNumberChange
        )
    }
}

@Composable
private fun CertificateTextField(
    certificateNumber: TextFieldValue,
    onCertificateNumberChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = certificateNumber,
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



