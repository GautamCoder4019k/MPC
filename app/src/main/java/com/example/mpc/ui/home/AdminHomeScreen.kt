package com.example.mpc.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt
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
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue
import com.example.mpc.ui.theme.LightGray

@Composable
fun AdminHomeScreen(modifier: Modifier = Modifier, onAddStudentClicked: () -> Unit = {}) {
    WaveShape()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        ButtonWithIcon(
            onClick = onAddStudentClicked,
            icon = Icons.Default.PersonAddAlt,
            text = "Add Student"
        )
        Spacer(modifier = Modifier.height(24.dp))
        ButtonWithIcon(
            onClick = onAddStudentClicked,
            icon = Icons.Default.PersonAddAlt,
            text = "Add Student"
        )
    }
}

@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String = ""
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = LightGray),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier.shadow(elevation = 8.dp)
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = DarkBlue, fontWeight = FontWeight.Bold)

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AdminHomeScreenPreview() {
    AdminHomeScreen()
}
