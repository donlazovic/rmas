package com.example.rmasprojekat18723.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rmasprojekat18723.Screens.HomeScreen
import com.example.rmasprojekat18723.Screens.LoginScreen
import com.example.rmasprojekat18723.Screens.MapScreen
import com.example.rmasprojekat18723.Screens.SignUpScreen
import com.example.rmasprojekat18723.Screens.ObjectTableScreen
import com.example.rmasprojekat18723.Screens.UserLeaderboardScreen

sealed class Route() {
    data class LoginScreen(val name: String = "Login") : Route()
    data class SignUpScreen(val name: String = "SignUp") : Route()
    data class HomeScreen(val name: String = "Home") : Route()
    data class MapScreen(val name: String = "Map") : Route()
    data class ObjectTableScreen(val name: String = "Object") : Route()
    data class UserLeaderboardScreen(val name: String = "UserLeaderboard") : Route()

}

@Composable
fun MyNavigation(navHostController: NavHostController, isUserLoggedIn: Boolean) {
    val startDestination = if (isUserLoggedIn) {
        Route.HomeScreen().name
    } else {
        "login_flow"
    }
    NavHost(navController = navHostController , startDestination = startDestination) {
        navigation(startDestination = Route.LoginScreen().name, route = "login_flow") {
            composable(route = Route.LoginScreen().name) {
                LoginScreen(
                    onLoginSuccess = {
                        navHostController.navigate(Route.HomeScreen().name) {
                            popUpTo(Route.LoginScreen().name) {
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
                SignUpScreen(onSignupSuccess = {
                    navHostController.navigate(Route.LoginScreen().name) {
                        popUpTo(Route.SignUpScreen().name) {
                            inclusive = true
                        }
                    }
                })
            }
        }
        composable(route = Route.HomeScreen().name) {
            HomeScreen(
                signOut = {
                    navHostController.navigate(Route.LoginScreen().name) {
                        popUpTo(Route.HomeScreen().name) {
                            inclusive = true
                        }
                    }
                },
                mapClick = {
                    navHostController.navigate(Route.MapScreen().name)
                },
                showAllObjects = {
                    navHostController.navigate(Route.ObjectTableScreen().name)
                },
                userLeaderboard = {
                    navHostController.navigate(Route.UserLeaderboardScreen().name)
                },
            )
        }
        composable(route = Route.MapScreen().name) {
            MapScreen(onSuccess = {
                navHostController.popBackStack()
            })
        }
        composable(route = Route.ObjectTableScreen().name) {
            ObjectTableScreen(goBackToHomeScreen = {
                navHostController.popBackStack()
            })
        }
        composable(route = Route.UserLeaderboardScreen().name) {
            UserLeaderboardScreen(onBack = {
                navHostController.popBackStack()
            })
        }
    }
}