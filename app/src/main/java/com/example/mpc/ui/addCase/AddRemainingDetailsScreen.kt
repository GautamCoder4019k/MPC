package com.example.mpc.ui.addCase

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mpc.ui.admin.addStudent.OutlinedTextFieldWithHeading
import com.example.mpc.ui.components.CustomDropdownMenu
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddRemainingDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    viewModel: AddCaseScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    RequestMicrophonePermission { granted ->
        viewModel.isPermissionGranted.value = granted
    }
    WaveShape()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(180.dp))
            Text(
                text = "Enter Superintendent Details:",
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextFieldWithHeading(
                text = "Name",
                placeholder = "Enter Name",
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.superintendentName,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                onValueChange = { viewModel.onSuperintendentNameChange(it) }
            )
            CustomDropdownMenu(
                text = "Select Department",
                modifier = Modifier.padding(vertical = 8.dp),
                placeholder = "--Select--",
                onValueChange = {
                    viewModel.onSuperintendentDepartmentChange(it)
                },
                options = listOf("CSE", "CSBS", "ISE")
            )
            OutlinedTextFieldWithHeading(
                text = "Contact No",
                placeholder = "Enter Contact No",
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.superintendentContactNo,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { viewModel.onSuperintendentContactNoChange(it) }
            )
        }
        Text(
            text = "Statements",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        RecordBox(
            text = "Student",
            modifier = Modifier.fillMaxWidth(0.7f),
            onRecordClick = { viewModel.startRecording() },
            onStopClick = { viewModel.stopRecordingAndUpload(type = "student") },
            onStartPlaying = { viewModel.playAudio(uiState.studentStatement) },
            onStopPlaying = { viewModel.stopAudio() },
            playUrl = uiState.studentStatement,
            fileName = "Student Statement"
        )
        OutlinedTextFieldWithHeading(
            text = "Squad Member",
            placeholder = "Enter statement",
            minLines = 6,
            maxLines = 6,
            modifier = Modifier.padding(top = 4.dp),
            value = uiState.squadMemberStatement,
            onValueChange = { viewModel.onSquadMemberStatementChange(it) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            enabled = uiState.isRemainingDataValid(),
            onClick = {
                navigateToHome()
            },
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

@Composable
fun RecordBox(
    text: String,
    modifier: Modifier = Modifier,
    onStartPlaying: () -> Unit = {},
    onStopPlaying: () -> Unit = {},
    onRecordClick: () -> Unit = {},
    playUrl: String = "",
    fileName: String = "",
    onStopClick: () -> Unit = {}
) {
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            color = DarkBlue,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp)
            )
            if (playUrl.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = fileName)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if (isPlaying) {
                                onStopPlaying()
                                isPlaying = false
                            } else {
                                onStartPlaying()
                                isPlaying = true
                            }
                        }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }

                }
            } else {
                OutlinedButton(
                    onClick = {
                        if (isRecording) {
                            onStopClick()
                        } else {
                            onRecordClick()
                        }
                        isRecording = !isRecording
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isRecording) "Stop" else "Record")
                }

            }

        }
    }
}

@Composable
fun RequestMicrophonePermission(
    onPermissionResult: (Boolean) -> Unit
) {
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            onPermissionResult(isGranted)
        }
    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}