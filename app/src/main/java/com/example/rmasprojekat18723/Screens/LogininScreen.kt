package com.example.rmasprojekat18723.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rmasprojekat18723.Components.DontHaveAccount
import com.example.rmasprojekat18723.Components.LoginTextComponent
import com.example.rmasprojekat18723.Components.SignUpTextComponent
import com.example.rmasprojekat18723.Components.TextFieldComponent
import com.example.rmasprojekat18723.R
import com.example.rmasprojekat18723.ui.theme.Secondary

@Composable
fun LoginScreen(onLoginClick: () -> Unit,onSignUpClick: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFieldsEmpty = username.isNotEmpty() && password.isNotEmpty()

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
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Username),
                value = username,
                onValueChange = { username = it },
                leadingIcon = Icons.Default.AccountCircle,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Password),
                value = password,
                onValueChange = { password = it },
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),)
            Spacer(modifier = Modifier.height(30.dp))
            Button( onClick = onLoginClick , modifier = Modifier.fillMaxWidth() , enabled = isFieldsEmpty) {
                Text("Login")
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