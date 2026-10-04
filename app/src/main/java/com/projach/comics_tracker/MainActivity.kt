package com.projach.comics_tracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projach.comics_tracker.domain.utils.Logger
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.navigation.navigate
import com.projach.comics_tracker.ui.screens.authCode.AuthUserScreen
import com.projach.comics_tracker.ui.screens.authCode.AuthUserViewModel
import com.projach.comics_tracker.ui.screens.loginUser.LoginUserScreen
import com.projach.comics_tracker.ui.screens.registerUser.RegisterUserScreen
import com.projach.comics_tracker.ui.screens.registerUser.RegisterUserViewModel
import com.projach.comics_tracker.ui.theme.ComicsTrackerTheme
import org.koin.compose.viewmodel.koinViewModel

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
                    ) {
                        composable<Screen.RegisterUserScreen> {
                            val viewModel: RegisterUserViewModel = koinViewModel()
                            LaunchedEffect(Unit) {
                                viewModel.navigationEvent.collect { action ->
                                    navigate(
                                        navController = navController,
                                        navigationAction = action
                                    )
                                }
                            }
                            LaunchedEffect(Unit) {
                                viewModel.errorEvent.collect { error ->
                                    Logger.d("MAIN-ACTIVITY", "error collected is $error")
                                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            RegisterUserScreen(viewModel)
                        }
                        composable<Screen.AuthUserScreen> {
                            val viewModel: AuthUserViewModel = koinViewModel()
                            LaunchedEffect(Unit) {
                                viewModel.navigationEvent.collect { action ->
                                    navigate(
                                        navController = navController,
                                        navigationAction = action
                                    )
                                }
                            }
                            AuthUserScreen(
                                viewModel
                            )
                        }
                        composable<Screen.LoginUserScreen> {
                            LoginUserScreen()
                        }
                    }
                }
            }
        }
    }
}