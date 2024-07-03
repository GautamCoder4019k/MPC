package com.example.mpc.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.mpc.ui.theme.DarkBlue

@Composable
fun WaveShape(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        val path = Path().apply {
            moveTo(0f, size.height)
            cubicTo(
                size.width * 0.2f, size.height,
                size.width * 0.6f, -size.height * 0.1f,
                size.width, size.height * 0.55f
            )
            lineTo(size.width, 0f)
            lineTo(0f, 0f)
            close()
        }
        drawPath(
            path = path,
            color = DarkBlue,
            style = androidx.compose.ui.graphics.drawscope.Fill
        )
    }
}

@Composable
fun CommonProgressBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .alpha(0.8f)
            .fillMaxSize()
            .background(Color.Gray),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}







