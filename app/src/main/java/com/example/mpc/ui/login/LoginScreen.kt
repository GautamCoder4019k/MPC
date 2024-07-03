package com.example.mpc.ui.login


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mpc.R
import com.example.mpc.ui.components.CommonProgressBar
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    navigateToAdminHome: () -> Unit = {}
) {
    val loginScreenViewModel: LoginScreenViewModel = hiltViewModel()
    val uiState by loginScreenViewModel.uiState.collectAsState()
    val context = LocalContext.current
    if (loginScreenViewModel.inProgress.value) {
        CommonProgressBar()
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        WaveShape()
        Image(
            painter = painterResource(id = R.drawable.dsi),
            contentDescription = "DSI logo",
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(0.5f),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Login",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = DarkBlue

        )
        Spacer(modifier = Modifier.size(16.dp))
        TextFieldWithoutBorder(
            value = uiState.email,
            labelText = "username",
            icon = R.drawable.user,
            iconsSize = 32.dp,
            onValueChange = { loginScreenViewModel.onEmailChange(it) })
        TextFieldWithoutBorder(
            value = uiState.password,
            labelText = "password",
            icon = R.drawable.padlock,
            iconsSize = 32.dp,
            onValueChange = { loginScreenViewModel.onPasswordChange(it) }
        )
        Spacer(modifier = Modifier.size(24.dp))
        Button(
            onClick = {
                loginScreenViewModel.loginWithEmail(
                    onSuccess = {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        if (loginScreenViewModel.isAdmin.value)
                            navigateToAdminHome()
                        else
                            navigateToHome()
                    },
                    onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            shape = RoundedCornerShape(16.dp),
            enabled = uiState.email.isNotEmpty() && uiState.password.isNotEmpty()
        ) {
            Text(
                text = "Login",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TextFieldWithoutBorder(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    labelText: String,
    icon: Int,
    iconsSize: Dp
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = labelText, color = DarkBlue) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(iconsSize)
                )
            }, modifier = Modifier.fillMaxWidth(0.725f)
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.65f), color = DarkBlue)
        Spacer(modifier = Modifier.size(16.dp))
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
