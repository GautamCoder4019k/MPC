package com.example.mpc.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpc.R
import com.example.mpc.ui.theme.DarkBlue
import com.example.mpc.ui.theme.LightGray

@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String = "",
    color: Color=Color.Black
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = LightGray),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    @DrawableRes iconId: Int = R.drawable.record_icon,
    text: String = "Records"
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = LightGray),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

        }
    }
}