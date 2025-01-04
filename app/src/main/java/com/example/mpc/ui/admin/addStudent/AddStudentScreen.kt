package com.example.mpc.ui.admin.addStudent

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mpc.ui.components.CommonProgressBar
import com.example.mpc.ui.components.CustomDropdownMenu
import com.example.mpc.ui.components.MyOutlinedTextField
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue

@Composable
fun AddStudentScreen(modifier: Modifier = Modifier) {
    val viewModel: AddStudentScreenViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    if (viewModel.isInProgress.value) {
        CommonProgressBar()
    }
    WaveShape()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(100.dp))
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
            modifier = Modifier.padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) }
        )
        CustomDropdownMenu(
            text = "Branch",
            placeholder = "Enter Branch",
            modifier = Modifier.padding(vertical = 8.dp),
            value = uiState.branch,
            onValueChange = { viewModel.onBranchChange(it) },
            options = listOf("CSE", "ISE", "CSBS", "CSDS")
        )
        CustomDropdownMenu(
            text = "Sem",
            placeholder = "Enter Sem",
            modifier = Modifier.padding(vertical = 8.dp),
            value = uiState.sem,
            onValueChange = { viewModel.onSemChange(it) },
            options = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII")
        )
        OutlinedTextFieldWithHeading(
            text = "Contact No",
            placeholder = "Enter Contact No",
            modifier = Modifier.padding(vertical = 8.dp),
            value = uiState.contactNo,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = { viewModel.onContactNoChange(it) }
        )
        OutlinedTextFieldWithHeading(
            text = "Email",
            placeholder = "Enter Email",
            modifier = Modifier.padding(vertical = 8.dp),
            value = uiState.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            onValueChange = { viewModel.onEmailChange(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = uiState.isValid(),
            onClick = {
                viewModel.addStudent(
                    onSuccess = {
                        Toast.makeText(context, "Student Added", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            shape = RoundedCornerShape(16.dp),

            ) {
            Text(
                text = "Add",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun OutlinedTextFieldWithHeading(
    modifier: Modifier = Modifier,
    value: String = "",
    readOnly: Boolean = false,
    maxLines: Int = 1,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit = {},
    text: String = "USN",
    placeholder: String = "Enter USN",
    textFieldModifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            color = DarkBlue,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        MyOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            modifier = textFieldModifier,
            minLines = minLines,
            maxLines = maxLines,
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddStudentScreenPreview() {
    AddStudentScreen()
}