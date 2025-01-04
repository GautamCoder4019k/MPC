package com.example.mpc.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpc.R
import com.example.mpc.ui.components.ButtonWithIcon
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.navigation.AddCase
import com.example.mpc.ui.navigation.Login
import com.example.mpc.ui.navigation.Record

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigateTo: (Any) -> Unit = {}) {
    WaveShape()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonWithIcon(
            onClick = { navigateTo(AddCase) },
            iconId = R.drawable.new_case_icon,
            text = "New Case",
            modifier = Modifier.fillMaxWidth(0.46f)
        )
        Spacer(modifier = Modifier.height(48.dp))
        ButtonWithIcon(
            onClick = { navigateTo(Record) },
            icon = Icons.Default.Book,
            text = "Records",
            modifier = Modifier.fillMaxWidth(0.46f)
        )
        Spacer(modifier = Modifier.height(64.dp))
        ButtonWithIcon(
            onClick = { navigateTo(Login) },
            icon = Icons.Default.PowerSettingsNew,
            text = "Log Out",
            modifier = Modifier.fillMaxWidth(0.46f),
            color= Color.Red
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}