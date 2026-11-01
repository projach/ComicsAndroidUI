package com.projach.comics_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.navigation.navigate
import com.projach.comics_tracker.ui.screens.otp.OtpScreen
import com.projach.comics_tracker.ui.screens.register.RegisterUserScreen
import com.projach.comics_tracker.ui.screens.register.RegisterUserViewModel
import com.projach.comics_tracker.ui.theme.ComicsTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ComicsTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.RegisterUserScreen,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable<Screen.RegisterUserScreen> {
                            val viewModel: RegisterUserViewModel = hiltViewModel()
                            LaunchedEffect(Unit) {
                                viewModel.navigationEvent.collect { action ->
                                    navigate(navController = navController, navigationAction = action)
                                }
                            }
                            RegisterUserScreen(viewModel)
                        }
                        composable<Screen.OtpScreen> {
                            OtpScreen()
                        }
                    }
                }
            }
        }
    }
}