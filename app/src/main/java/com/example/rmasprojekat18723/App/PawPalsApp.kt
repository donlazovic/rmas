
package com.example.rmasprojekat18723.App

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.rmasprojekat18723.Navigation.MyNavigation
import com.example.rmasprojekat18723.Screens.LoginScreen
import com.example.rmasprojekat18723.Screens.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PawPalsApps() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

    }
    val navController = rememberNavController()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val isUserLoggedIn = currentUser != null

    MyNavigation(navHostController = navController, isUserLoggedIn = isUserLoggedIn)

}
