package com.example.mpc.ui.admin.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpc.ui.components.ButtonWithIcon
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.navigation.AddSquadMember
import com.example.mpc.ui.navigation.AddStudent
import com.example.mpc.ui.navigation.Login
import com.example.mpc.ui.navigation.Record
import com.example.mpc.ui.theme.DarkBlue
import com.example.mpc.ui.theme.LightGray

@Composable
fun AdminHomeScreen(
    modifier: Modifier = Modifier,
    navigateTo: (Any) -> Unit = {}
) {
    WaveShape()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        ButtonWithIcon(
            onClick = { navigateTo(AddStudent) },
            icon = Icons.Default.PersonAddAlt,
            text = "Add Student",
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Spacer(modifier = Modifier.height(64.dp))
        ButtonWithIcon(
            onClick = { navigateTo(AddSquadMember) },
            icon = Icons.Default.PersonAddAlt,
            text = "Add Squad Member",
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Spacer(modifier = Modifier.height(64.dp))
        ButtonWithIcon(
            onClick = { navigateTo(Record) },
            icon = Icons.Default.Book,
            text = "Records",
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Spacer(modifier = Modifier.height(64.dp))
        ButtonWithIcon(
            onClick = { navigateTo(Login) },
            icon = Icons.Default.PowerSettingsNew,
            text = "Log Out",
            modifier = Modifier.fillMaxWidth(0.6f),
            color=Color.Red
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun AdminHomeScreenPreview() {
    AdminHomeScreen()
}
