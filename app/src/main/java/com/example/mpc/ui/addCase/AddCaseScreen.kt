package com.example.mpc.ui.addCase

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mpc.ui.admin.addStudent.OutlinedTextFieldWithHeading
import com.example.mpc.ui.components.CustomDropdownMenu
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCaseScreen(
    modifier: Modifier = Modifier,
    navigateToNextScreen: () -> Unit = {},
    viewModel: AddCaseScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    WaveShape()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(150.dp))
            Text(
                text = "Enter Details",
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextFieldWithHeading(
                text = "USN",
                placeholder = "Enter USN",
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.usn,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onValueChange = { viewModel.onUsnChange(it) }
            )
            OutlinedTextFieldWithHeading(
                text = "Name",
                placeholder = "Enter Name",
                readOnly = true,
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.name,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onValueChange = { viewModel.onNameChange(it) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextFieldWithHeading(
                    text = "Date",
                    placeholder = "DD/MM/YYYY",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(1f),
                    readOnly = true,
                    textFieldModifier = Modifier.fillMaxWidth(),
                    value = uiState.date,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )
                Spacer(modifier = Modifier.width(24.dp))
                OutlinedTextFieldWithHeading(
                    text = "Time",
                    placeholder = "HH:MM",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(1f),
                    readOnly = true,
                    value = uiState.time,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),

                )
            }
            CustomDropdownMenu(
                text = "Course Code",
                modifier = Modifier.padding(vertical = 8.dp),
                placeholder = "Select Course",
                onValueChange = {
                    viewModel.onCourseCodeChange(it)
                },
                options = listOf("22MATS021", "22CHEM021", "22ESC022")
            )
            CustomDropdownMenu(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Nature of Malpractice",
                placeholder = "--Select--",
                onValueChange = {
                    viewModel.onNatureChange(it)
                },
                options = listOf("chits", "written on palm", "phone", "others")
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            enabled = uiState.isDataValid(),
            onClick = navigateToNextScreen,
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "Next",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddCaseScreenPreview() {
    AddCaseScreen()
}