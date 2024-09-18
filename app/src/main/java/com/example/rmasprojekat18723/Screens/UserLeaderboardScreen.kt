package com.example.rmasprojekat18723.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rmasprojekat18723.data.RegistrationUIState
import com.example.rmasprojekat18723.data.SignUpViewModel
import com.example.rmasprojekat18723.data.SignUpUIEvent
import com.example.rmasprojekat18723.ui.theme.BronzeColor
import com.example.rmasprojekat18723.ui.theme.GoldColor
import com.example.rmasprojekat18723.ui.theme.SilverColor


@Composable
fun UserLeaderboardScreen(signUpViewModel: SignUpViewModel = viewModel(), onBack: () -> Unit) {
    val users by signUpViewModel.usersState

    LaunchedEffect(Unit) {
        signUpViewModel.onEvent(SignUpUIEvent.LoadUsers)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Leaderboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(users.size) { index ->
                    UserCard(user = users[index], position = index + 1)
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun UserCard(user: RegistrationUIState, position: Int) {

    Log.d("UserCard", "User image URL: ${user.profileImageUri}")

    val medalColor = when (position) {
        1 -> GoldColor
        2 -> SilverColor
        3 -> BronzeColor
        else -> Color.Black
    }

    val size = if (position <= 3) 50.dp else 24.dp

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$position",
                    fontSize = size.value.sp,
                    fontWeight = FontWeight.Bold,
                    color = medalColor,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Image(
                    painter = rememberAsyncImagePainter(user.profileImageUri),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user.username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F618D),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Text(
                text = "${user.points}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

