package com.example.rmasprojekat18723.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.rmasprojekat18723.data.ObjectUIEvent
import com.example.rmasprojekat18723.data.ObjectViewModel

@Composable
fun AddObjectScreen(objectViewModel: ObjectViewModel = viewModel(), onSuccess: () -> Unit) {
    val objectState by objectViewModel.objectUIState
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        objectViewModel.onEvent(ObjectUIEvent.PhotoSelected(uri))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Object",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TextField(
            value = objectState.title,
            onValueChange = { objectViewModel.onEvent(ObjectUIEvent.TitleChange(it)) },
            label = { Text("Title") },
            isError = objectState.titleError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = objectState.description,
            onValueChange = { objectViewModel.onEvent(ObjectUIEvent.DescriptionChange(it)) },
            label = { Text("Description") },
            isError = objectState.descriptionError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = objectState.startTime,
            onValueChange = { objectViewModel.onEvent(ObjectUIEvent.StartTimeChange(it)) },
            label = { Text("Example: 16:45") },
            isError = objectState.startTimeError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )


        TextField(
            value = objectState.duration,
            onValueChange = { objectViewModel.onEvent(ObjectUIEvent.DurationChange(it)) },
            label = { Text("Duration (hours,minutes)") },
            isError = objectState.durationError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Choose Photo")
        }

        selectedImageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(128.dp)
                    .padding(vertical = 16.dp)
            )
        }

        Button(
            onClick = { objectViewModel.onEvent(ObjectUIEvent.AddObjectClicked(onSuccess)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Add Object")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddObjectPreview() {
    AddObjectScreen {}
}