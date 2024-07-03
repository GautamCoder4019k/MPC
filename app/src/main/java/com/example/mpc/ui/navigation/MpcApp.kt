package com.example.mpc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpc.ui.addStudent.AddStudentScreen
import com.example.mpc.ui.home.AdminHomeScreen
import com.example.mpc.ui.home.HomeScreen
import com.example.mpc.ui.login.LoginScreen

@Composable
fun MpcApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(navigateToHome = {
                navController.navigate(Home) {
                    popUpTo(Login) {
                        inclusive = true
                    }
                }

            },
                navigateToAdminHome = {
                    navController.navigate(AdminHome) {
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Home> {
            HomeScreen()
        }
        composable<AdminHome> {
            AdminHomeScreen(onAddStudentClicked = { navController.navigate(AddStudent) })
        }
        composable<AddStudent> {
            AddStudentScreen()
        }

    }
}