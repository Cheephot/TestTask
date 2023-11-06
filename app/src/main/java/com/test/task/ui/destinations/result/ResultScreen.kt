package com.test.task.ui.destinations.result

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.task.R
import com.ramcosta.composedestinations.annotation.Destination
import com.test.task.ui.RootNavGraph

@Composable
@Destination
@RootNavGraph
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        InfoText(
            name = R.string.state_registration_plate,
            info = viewModel.stateRegistrationPlate
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoText(
            name = R.string.certificate_number,
            info = viewModel.certificateNumber
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoText(
            name = R.string.driver_license_number,
            info = viewModel.driverLicenseNumber
        )
    }
}

@Composable
fun InfoText(
    @StringRes name: Int,
    info: String?
) {
    val infoText = remember(info) { if (info.isNullOrEmpty()) "-" else info }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(name),
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = infoText,
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}