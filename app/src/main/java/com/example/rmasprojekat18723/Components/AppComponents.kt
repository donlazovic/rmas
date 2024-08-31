package com.example.rmasprojekat18723.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rmasprojekat18723.ui.theme.BgColor
import com.example.rmasprojekat18723.ui.theme.Primary
import com.example.rmasprojekat18723.ui.theme.TextColor
import com.example.rmasprojekat18723.ui.theme.TextFieldColor

@Composable
fun SignUpTextComponent(value : String , modifier : Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        color = TextColor,
        textAlign = TextAlign.Center
    )

}

@Composable
fun LoginTextComponent(value : String , modifier : Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        color = TextColor,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    label : String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions : KeyboardOptions,
    leadingIcon: ImageVector? = null,
    visualTransformation : VisualTransformation = VisualTransformation.None
) {

    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label,
            color = TextFieldColor) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Primary,
                focusedLabelColor = Primary,
                cursorColor = Primary,
                containerColor = BgColor
    ),

        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(30),
        leadingIcon = {if (leadingIcon!=null) Icon(imageVector = leadingIcon,null )},
        visualTransformation = visualTransformation,
        value = value ,
        onValueChange = onValueChange

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldComponent(
     label : String,

) {
    val password = remember {mutableStateOf("") }

    val passwordVisible = remember {mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text(text = label,
            color = TextFieldColor) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            containerColor = BgColor
        ),

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value ,
        onValueChange = { password.value = it},

    )
}

@Composable
fun DontHaveAccount(onSignUpClick: () -> Unit ,modifier: Modifier = Modifier) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Don't have an account?", color = Color.White , fontSize = 16.sp)
        Spacer(Modifier.width(16.dp))
        TextButton(onClick = onSignUpClick) {
            Text("Register Here" , fontSize = 16.sp)

            }
            
        }

    }

