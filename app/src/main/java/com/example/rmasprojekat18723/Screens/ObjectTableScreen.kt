package com.example.rmasprojekat18723.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.rmasprojekat18723.data.ObjectUIEvent
import com.example.rmasprojekat18723.data.ObjectUIState
import com.example.rmasprojekat18723.data.ObjectViewModel

@Composable
fun ObjectTableScreen( goBackToHomeScreen: () -> Unit, viewModel: ObjectViewModel = viewModel()) {

    val objectState by viewModel.objectUIState

    LaunchedEffect(Unit) {
        viewModel.onEvent(ObjectUIEvent.LoadAllObjects)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "All Registered Objects",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            val sortedObjects = objectState.objects.sortedByDescending { it.avgGrade }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) {
                items(sortedObjects.size) { index ->
                    ObjectCard(sortedObjects[index])
                }
            }


            Button(
                onClick = {
                    goBackToHomeScreen()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Go Back to Home Screen")
            }
        }
    }
}


@Composable
fun ObjectCard(obj: ObjectUIState) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(obj.photoUri),
                contentDescription = "Object Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Title: ${obj.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Description: ${obj.description}",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Duration: ${obj.duration}",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Start Time: ${obj.startTime}",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Average Grade: ${obj.avgGrade}",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}