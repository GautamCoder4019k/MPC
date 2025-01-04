package com.example.mpc.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mpc.ui.addCase.AddCaseScreen
import com.example.mpc.ui.addCase.AddCaseScreenViewModel
import com.example.mpc.ui.addCase.AddProofScreen
import com.example.mpc.ui.addCase.AddRemainingDetailsScreen
import com.example.mpc.ui.admin.addSquadMember.AddSquadMemberScreen
import com.example.mpc.ui.admin.addStudent.AddStudentScreen
import com.example.mpc.ui.admin.home.AdminHomeScreen
import com.example.mpc.ui.caseDetail.CaseDetailScreen
import com.example.mpc.ui.home.HomeScreen
import com.example.mpc.ui.login.LoginScreen
import com.example.mpc.ui.login.LoginScreenViewModel
import com.example.mpc.ui.record.FilterScreen
import com.example.mpc.ui.record.RecordScreen
import com.example.mpc.ui.record.RecordViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MpcApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val addCaseScreenViewModel: AddCaseScreenViewModel = hiltViewModel()
    val recordViewModel: RecordViewModel = hiltViewModel()
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
            HomeScreen(navigateTo = { navController.navigate(it) })
        }
        composable<AdminHome> {
            AdminHomeScreen(navigateTo = { navController.navigate(it) }
            )
        }
        composable<AddStudent> {
            AddStudentScreen()
        }
        composable<AddSquadMember> {
            AddSquadMemberScreen()
        }
        composable<Record> {
            RecordScreen(
                onCaseClick = {
                    navController.navigate(CaseDetail(it))
                },
                navigateToFilter = { navController.navigate(Filter) },
                viewModel = recordViewModel
            )
        }
        composable<Filter> {
            FilterScreen(viewModel = recordViewModel)
        }
        composable<CaseDetail> {
            val args = it.toRoute<CaseDetail>()
            CaseDetailScreen(
                id = args.id, onCaseClick = { caseId ->
                    navController.navigate(CaseDetail(caseId))
                }, navigateBack = { navController.navigateUp() }
            )
        }
        composable<AddCase> {
            AddCaseScreen(
                navigateToNextScreen = { navController.navigate(AddRemainingDetails) },
                viewModel = addCaseScreenViewModel
            )
        }
        composable<AddRemainingDetails> {
            AddRemainingDetailsScreen(
                navigateToHome = {
                    navController.navigate(AddProof)
                }, viewModel = addCaseScreenViewModel
            )
        }
        composable<AddProof> {
            AddProofScreen(
                viewModel = addCaseScreenViewModel,
                navigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Home) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}