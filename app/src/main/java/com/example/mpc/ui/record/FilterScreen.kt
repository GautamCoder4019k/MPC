package com.example.mpc.ui.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FilterScreen(modifier: Modifier = Modifier, viewModel: RecordViewModel = hiltViewModel()) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .padding(top = 24.dp)
    ) {
        FilterWithOptions(
            filterName = "Nature",
            options = listOf("chits", "written on palm", "phone", "others"),
            selectedOptions = viewModel.selectedSems,
            onOptionSelected = { viewModel.selectedSems = it }
        )
        FilterWithOptions(
            filterName = "Course",
            options = listOf("22MATS021", "22CHEM021", "22ESC022"),
            selectedOptions = viewModel.selectedBranches,
            onOptionSelected = { viewModel.selectedBranches = it }
        )
        FilterWithOptions(
            filterName = "Status",
            options = listOf("Approved", "Pending"),
            selectedOptions = viewModel.selectedStatuses,
            onOptionSelected = { viewModel.selectedStatuses = it }
        )
    }
}

@Composable
fun FilterWithOptions(
    modifier: Modifier = Modifier,
    filterName: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionSelected: (Set<String>) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = filterName,
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        options.forEach { option ->
            val isSelected = selectedOptions.contains(option)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        val newSelectedOptions = if (isSelected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onOptionSelected(newSelectedOptions)
                    }
                )
                Text(text = option)
            }
        }
    }
}