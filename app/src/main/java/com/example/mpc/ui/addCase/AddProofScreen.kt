package com.example.mpc.ui.addCase

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mpc.ui.caseDetail.FullScreenImage
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddProofScreen(
    modifier: Modifier = Modifier,
    viewModel: AddCaseScreenViewModel = hiltViewModel(),
    navigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var activePhotos by remember { mutableStateOf("") }
    WaveShape()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(180.dp))
        Text(
            text = "Proof",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ImageBox(
            text = "Add Images",
            modifier = Modifier.fillMaxWidth(0.8f),
            onImagesSelected = { viewModel.uploadImagesToFirebase(it) },
            onImageClick = { activePhotos = it }
        )

        Button(
            enabled = uiState.proofImages.isNotEmpty(),
            onClick = {
                viewModel.saveCase()
                navigateToHome()
            },
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Submit",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
    if (activePhotos != "") {
        FullScreenImage(
            imageUrl = activePhotos,
            onDismiss = { activePhotos = "" }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageBox(
    text: String,
    onImagesSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit
) {
    var selectedImagesUri by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) {
            it.forEach {
                selectedImagesUri = selectedImagesUri + it.toString()
            }
            onImagesSelected(selectedImagesUri)
        }
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
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp)
            )

            OutlinedButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Select Images")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (selectedImagesUri.isNotEmpty()) {
            FlowRow {
                selectedImagesUri.forEach {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .clickable { onImageClick(it) }
                    )
                }
            }
        }
    }
}