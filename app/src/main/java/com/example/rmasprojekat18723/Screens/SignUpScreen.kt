package com.example.rmasprojekat18723.Screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rmasprojekat18723.Components.DontHaveAccount
import com.example.rmasprojekat18723.Components.PasswordFieldComponent
import com.example.rmasprojekat18723.Components.SignUpTextComponent
import com.example.rmasprojekat18723.Components.TextFieldComponent
import com.example.rmasprojekat18723.R
import com.example.rmasprojekat18723.ui.theme.Secondary


@Composable
fun SignUpScreen(onSignUpClick: () -> Unit)
{
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordSame by remember { mutableStateOf(false) }

    val isFieldsEmpty = username.isNotEmpty() && password.isNotEmpty()  && name.isNotEmpty()&& surname.isNotEmpty()&& phoneNumber.isNotEmpty()
            && email.isNotEmpty()  && confirmPassword.isNotEmpty()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary)
            .padding(32.dp),
        color = Secondary
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(isPasswordSame) {
                Text( text = "Passwords are not matching",
                    color = MaterialTheme.colorScheme.error)
                
            }
            SignUpTextComponent(value = stringResource(id = R.string.Registration), modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),)
            Spacer(modifier = Modifier.height(30.dp))
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
                label = stringResource(id = R.string.Name),
                value = name,
                onValueChange = { name = it },
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Surname),
                value = surname,
                onValueChange = { surname = it },
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Phonenumber),
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Email),
                value = email,
                onValueChange = { email = it },
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Confirmpassword),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),)
            Spacer(modifier = Modifier.height(30.dp))
            Button( onClick = {
                isPasswordSame = password != confirmPassword
                if(!isPasswordSame) {
                    onSignUpClick()
                }
            } , modifier = Modifier.fillMaxWidth(),
                enabled = isFieldsEmpty) {
                Text("Sign Up")
            }
        }


    }
}

@Preview (showBackground = true)
@Composable
fun PreviewSignUp() {
    SignUpScreen({})
}