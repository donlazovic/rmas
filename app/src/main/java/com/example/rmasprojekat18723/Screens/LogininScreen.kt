package com.example.rmasprojekat18723.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rmasprojekat18723.Components.DontHaveAccount
import com.example.rmasprojekat18723.Components.LoginTextComponent
import com.example.rmasprojekat18723.Components.TextFieldComponent
import com.example.rmasprojekat18723.R
import com.example.rmasprojekat18723.data.LogInUIEvent
import com.example.rmasprojekat18723.data.LogInViewModel
import com.example.rmasprojekat18723.ui.theme.ButtonColor1
import com.example.rmasprojekat18723.ui.theme.ButtonColor2
import com.example.rmasprojekat18723.ui.theme.Secondary

@Composable
fun LoginScreen(onSignUpClick: () -> Unit, onLoginSuccess: () -> Unit , loginViewModel: LogInViewModel = viewModel()) {

    val loginState = loginViewModel.loginUIState.value

    var passwordVisible by remember { mutableStateOf(false) }


    val isFieldsEmpty = loginState.email.isNotEmpty() && loginState.password.isNotEmpty()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary)
            .padding(32.dp),
        color = Secondary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginTextComponent(value = stringResource(id = R.string.Login), modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .align(alignment = Alignment.Start),)

            Spacer(modifier = Modifier.height(40.dp))

            loginState.loginError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Email),
                value = loginState.email,
                onValueChange = { loginViewModel.onEvent(LogInUIEvent.EmailChange(it))},
                leadingIcon = Icons.Default.AccountCircle,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorStatus = loginViewModel.loginUIState.value.emailError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Password),
                value = loginState.password,
                onValueChange = { loginViewModel.onEvent(LogInUIEvent.PasswordChange(it)) },
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible } .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                errorStatus = loginViewModel.loginUIState.value.passwordError)

            Spacer(modifier = Modifier.height(30.dp))

            Button( onClick = {loginViewModel.onEvent(LogInUIEvent.LoginClicked(onLoginSuccess))} , modifier = Modifier.fillMaxWidth() , colors = ButtonDefaults.buttonColors(
                Color.Transparent), enabled = isFieldsEmpty) {
                Box(modifier = Modifier.fillMaxWidth().heightIn(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                        shape = RoundedCornerShape(50.dp)
                    ),
                    contentAlignment = Alignment.Center) {
                    Text(text = "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,)
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            DontHaveAccount(onSignUpClick,modifier = Modifier.fillMaxSize().wrapContentSize(align = Alignment.BottomCenter))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    LoginScreen({},{})
}