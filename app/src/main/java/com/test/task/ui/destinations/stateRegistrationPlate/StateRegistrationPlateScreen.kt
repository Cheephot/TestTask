package com.test.task.ui.destinations.stateRegistrationPlate

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
import com.foresko.debts.ui.destinations.destinations.CertificateNumberScreenDestination
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
@RootNavGraph(start = true)
fun StateRegistrationPlateScreen(
    viewModel: StateRegistrationPlateViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    StateRegistrationPlateScreen(
        viewModel = viewModel,
        navigateToCertificateNumberScreen = {
            rootNavigator.navigate(CertificateNumberScreenDestination)
        },
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
private fun StateRegistrationPlateScreen(
    viewModel: StateRegistrationPlateViewModel,
    navigateToCertificateNumberScreen: () -> Unit,
    navigateToResultScreen: () -> Unit,
    navigateToDriverLicenseNumberScreen: () -> Unit
) {
    ContinueLaunch(continueState = viewModel.continueState) { navigateToCertificateNumberScreen() }

    SkipAlertDialog(
        text = R.string.skip_plate_description,
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
        text = R.string.enter_grz,
        continueHandler = viewModel::continueHandler,
        onSkipAlertDialogStateChange = viewModel::onSkipAlertDialogStateChange
    ) {
        PlateTextField(
            stateRegistrationPlate = viewModel.stateRegistrationPlate,
            onStateRegistrationPlateChange = viewModel::onStateRegistrationPlateChange
        )
    }
}

@Composable
private fun PlateTextField(
    stateRegistrationPlate: TextFieldValue,
    onStateRegistrationPlateChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = stateRegistrationPlate,
        onValueChange = onStateRegistrationPlateChange,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        visualTransformation = plateTransformation()
    )
}

private fun plateTransformation() = VisualTransformation { text ->
    val offsets = MutableList(text.length + 1) { -1 }

    val transformedText = buildString {
        var previousCharIsDigit = false
        var transformedIndex = 0

        for (index in text.indices) {
            val char = text[index]

            if (char == ' ') continue

            if (char.isDigit()) {
                if (!previousCharIsDigit && this.isNotEmpty()) {
                    append(' ')
                    transformedIndex++
                }

                append(char)
                offsets[index] = transformedIndex
                transformedIndex++
                previousCharIsDigit = true
            } else {
                if (previousCharIsDigit && this.isNotEmpty()) {
                    append(' ')
                    transformedIndex++
                }

                append(char)
                offsets[index] = transformedIndex
                transformedIndex++
                previousCharIsDigit = false
            }
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




