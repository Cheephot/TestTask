package com.test.task.ui.destinations.stateRegistrationPlate

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
import com.test.task.ui.destinations.destinations.CertificateNumberScreenDestination
import com.test.task.ui.destinations.destinations.DriverLicenseNumberScreenDestination
import com.test.task.ui.destinations.destinations.ResultScreenDestination
import com.test.task.ui.utils.showNotValidNumberToast

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
    val context = LocalContext.current

    val (skipAlertDialogState, onSkipAlertDialogStateChange) = remember { mutableStateOf(false) }

    val (stateRegistrationPlate, onStateRegistrationPlateChange) = remember {
        mutableStateOf(TextFieldValue(""))
    }

    SkipAlertDialog(
        text = R.string.skip_plate_description,
        skipAlertDialogState = skipAlertDialogState,
        onSkipAlertDialogStateChange = { onSkipAlertDialogStateChange(!skipAlertDialogState) },
        navigateTo = {
            if (viewModel.driverLicenseNumber.isNullOrEmpty()) {
                viewModel.onAutoInfoUpdate(stateRegistrationPlate.text)

                navigateToDriverLicenseNumberScreen()
            } else {
                navigateToResultScreen()
            }
        }
    )

    DefaultEnterNumbersUi(
        text = R.string.enter_grz,
        onContinueClick = {
            if (viewModel.validateRegistrationPlate(stateRegistrationPlate.text)) {
                viewModel.onAutoInfoUpdate(stateRegistrationPlate.text)

                navigateToCertificateNumberScreen()
            } else {
                showNotValidNumberToast(context)
            }
        },
        onSkipAlertDialogStateChange = { onSkipAlertDialogStateChange(!skipAlertDialogState) }
    ) {
        PlateTextField(
            stateRegistrationPlate = stateRegistrationPlate,
            onStateRegistrationPlateChange = onStateRegistrationPlateChange
        )
    }
}

@Composable
private fun PlateTextField(
    stateRegistrationPlate: TextFieldValue,
    onStateRegistrationPlateChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = stateRegistrationPlate.copy(text = stateRegistrationPlate.text.uppercase()),
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




