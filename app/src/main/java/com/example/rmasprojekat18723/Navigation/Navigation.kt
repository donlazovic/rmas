package com.example.rmasprojekat18723.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rmasprojekat18723.Screens.HomeScreen
import com.example.rmasprojekat18723.Screens.LoginScreen
import com.example.rmasprojekat18723.Screens.SignUpScreen

sealed class Route() {
    data class LoginScreen(val name: String = "Login") : Route()
    data class SignUpScreen(val name: String = "SignUp") : Route()
    data class HomeScreen(val name: String = "Home") : Route()

}

@Composable
fun MyNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController , startDestination = "login_flow") {
        navigation(startDestination = Route.LoginScreen().name, route = "login_flow") {
            composable(route = Route.LoginScreen().name) {
                LoginScreen(
                    onLoginClick =  {
                        navHostController.navigate(Route.HomeScreen().name) {
                            popUpTo("login_flow") {
                                inclusive = true
                            }
                        }
                    },
                    onSignUpClick = {
                        navHostController.navigate(Route.SignUpScreen().name)
                    }
                )
            }
            composable(route = Route.SignUpScreen().name) {
                SignUpScreen(onSignUpClick = {
                    navHostController.navigate(Route.LoginScreen().name)
                })
            }
        }
        composable(route = Route.HomeScreen().name) {
            HomeScreen()
        }
    }
}

