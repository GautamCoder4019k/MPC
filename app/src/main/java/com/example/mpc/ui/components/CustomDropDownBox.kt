package com.example.mpc.ui.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mpc.ui.addStudent.OutlinedTextFieldWithHeading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    value: String = "I",
    onValueChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf(value) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        OutlinedTextFieldWithHeading(
            text = text,
            placeholder = placeholder,
            value = selectedOptionText,
            readOnly = true,
            onValueChange = { selectedOptionText = it },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

            ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onValueChange(selectionOption)
                    },
                    text = { Text(selectionOption) }
                )
            }
        }
    }
}
