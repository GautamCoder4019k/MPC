package com.example.mpc

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mpc.ui.addCase.AddProofScreen
import com.example.mpc.ui.addCase.AddRemainingDetailsScreen
import com.example.mpc.ui.admin.addSquadMember.AddSquadMemberScreen
import com.example.mpc.ui.caseDetail.CaseDetailScreen
import com.example.mpc.ui.navigation.MpcApp
import com.example.mpc.ui.record.FilterScreen
import com.example.mpc.ui.record.RecordScreen
import com.example.mpc.ui.theme.MPCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MPCTheme {
                Scaffold(
                    modifier = Modifier
                ) { _ ->
//                    val addCaseScreenViewModel: AddCaseScreenViewModel = hiltViewModel()
                    MpcApp()
//                    FilterScreen()
                }
            }
        }
    }
}
