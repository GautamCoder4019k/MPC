package com.example.mpc.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mpc.ui.components.WaveShape

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column {
        WaveShape()
        Text(text = "Enter Details")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}