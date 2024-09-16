package com.example.rmasprojekat18723.Screens


import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.rememberImagePainter
import com.example.rmasprojekat18723.Components.SignUpTextComponent
import com.example.rmasprojekat18723.Components.TextFieldComponent
import com.example.rmasprojekat18723.R
import com.example.rmasprojekat18723.data.SignUpViewModel
import com.example.rmasprojekat18723.data.SignUpUIEvent
import com.example.rmasprojekat18723.ui.theme.ButtonColor1
import com.example.rmasprojekat18723.ui.theme.ButtonColor2
import com.example.rmasprojekat18723.ui.theme.Secondary
import android.graphics.Bitmap
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun SignUpScreen(onSignupSuccess: () -> Unit , signUpViewModel: SignUpViewModel = viewModel())
{
    val registrationState = signUpViewModel.registrationUIState.value

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            signUpViewModel.onEvent(SignUpUIEvent.ImageSelected(uri))
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            capturedImageBitmap = bitmap
            bitmap?.let {
                val uri = saveBitmapToFile(context, it)
                uri?.let { imageUri ->
                    selectedImageUri = imageUri
                    signUpViewModel.onEvent(SignUpUIEvent.ImageSelected(imageUri))
                }
            }
        }
    )

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var isPasswordSame by remember { mutableStateOf(false) }

    var isPasswordSameError by remember { mutableStateOf<String?>(null) }

    val isFieldsEmpty = registrationState.username.isNotEmpty() && registrationState.password.isNotEmpty()  && registrationState.name.isNotEmpty()&& registrationState.surname.isNotEmpty()&&
            registrationState.phoneNumber.isNotEmpty() && registrationState.email.isNotEmpty()  && registrationState.confirmPassword.isNotEmpty()
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
            isPasswordSameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
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
                value = registrationState.username,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.UsernameChange(it)) },
                leadingIcon = Icons.Default.AccountCircle,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorStatus = signUpViewModel.registrationUIState.value.usernameError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Name),
                value = registrationState.name,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.NameChange(it)) },
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorStatus = signUpViewModel.registrationUIState.value.nameError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Surname),
                value = registrationState.surname,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.SurnameChange(it)) },
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorStatus = signUpViewModel.registrationUIState.value.surnameError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Phonenumber),
                value = registrationState.phoneNumber,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.PhoneNumberChange(it)) },
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                errorStatus = signUpViewModel.registrationUIState.value.phoneNumberError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Email),
                value = registrationState.email,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.EmailChange(it))  },
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                errorStatus = signUpViewModel.registrationUIState.value.emailError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Password),
                value = registrationState.password,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.PasswordChange(it))},
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                },
                errorStatus = signUpViewModel.registrationUIState.value.passwordError)
            TextFieldComponent(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = stringResource(id = R.string.Confirmpassword),
                value = registrationState.confirmPassword,
                onValueChange = { signUpViewModel.onEvent(SignUpUIEvent.ConfirmPasswordChange(it)) },
                leadingIcon = Icons.Default.Lock,
                visualTransformation = if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    Text(
                        text = if (confirmPasswordVisible) "Hide" else "Show",
                        modifier = Modifier
                            .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    },
                errorStatus = signUpViewModel.registrationUIState.value.confirmPasswordError)
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                onClick = { showDialog = true }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pick or Take a Photo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            if (showDialog) {
                ShowPhotoSourceDialog(
                    onDismiss = { showDialog = false },
                    onCameraSelected = {
                        takePictureLauncher.launch(null)
                        showDialog = false
                    },
                    onGallerySelected = {
                        pickImageLauncher.launch("image/*")
                        showDialog = false
                    }
                )
            }

            selectedImageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Selected image",
                    modifier = Modifier.size(128.dp)
                )
            } ?: capturedImageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier.size(128.dp)
                )
            }


            Spacer(modifier = Modifier.height(10.dp))

            Button( onClick = {
                isPasswordSame = registrationState.password != registrationState.confirmPassword
                if(!isPasswordSame) {
                    isPasswordSameError = null
                    signUpViewModel.onEvent(SignUpUIEvent.RegisterClicked(onSignupSuccess))
                }
                else {
                    isPasswordSameError = "Passwords do not match."
                }

            } , modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                enabled = isFieldsEmpty) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                        shape = RoundedCornerShape(50.dp)
                    ),
                    contentAlignment = Alignment.Center) {
                    Text(text = "Sign Up",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,)
                }
            }

        }


    }
}

@Composable
fun ShowPhotoSourceDialog(
    onDismiss: () -> Unit,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Choose photo source") },
        text = { Text(text = "Please select a photo source: Camera or Gallery.") },
        confirmButton = {
            Button(onClick = {
                onCameraSelected()
                onDismiss()
            }) {
                Text("Camera")
            }
        },
        dismissButton = {
            Button(onClick = {
                onGallerySelected()
                onDismiss()
            }) {
                Text("Gallery")
            }
        }
    )
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
    val fileName = "${System.currentTimeMillis()}.jpg"
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(directory, fileName)
    var outputStream: FileOutputStream? = null

    try {
        outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        return Uri.fromFile(imageFile)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        outputStream?.close()
    }
    return null
}




@Preview (showBackground = true)
@Composable
fun PreviewSignUp() {
    SignUpScreen({})
}