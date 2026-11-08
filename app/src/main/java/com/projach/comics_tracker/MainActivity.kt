package com.projach.comics_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.navigation.navigate
import com.projach.comics_tracker.ui.screens.login.LoginUserScreen
import com.projach.comics_tracker.ui.screens.register.RegisterEffect
import com.projach.comics_tracker.ui.screens.register.RegisterUserScreen
import com.projach.comics_tracker.ui.screens.register.RegisterUserViewModel
import com.projach.comics_tracker.ui.screens.verifyOtp.OtpScreen
import com.projach.comics_tracker.ui.screens.verifyOtp.OtpViewModel
import com.projach.comics_tracker.ui.screens.verifyOtp.VerifyOtpEffect
import com.projach.comics_tracker.ui.theme.ComicsTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            ComicsTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.RegisterUserScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.RegisterUserScreen> {
                            val viewModel: RegisterUserViewModel = hiltViewModel()
                            LaunchedEffect(Unit) {
                                viewModel.effect.collect { effect ->
                                    when (effect) {
                                        is RegisterEffect.ShowError -> {
                                            Logger.d("MainActivity", "Showing error")
                                            snackbarHostState.showSnackbar(
                                                message = effect.message,
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        }

                                        is RegisterEffect.Navigate -> {
                                            navigate(
                                                navController = navController,
                                                navigationAction = effect.navigation
                                            )
                                        }
                                    }
                                }
                            }
                            RegisterUserScreen(viewModel)
                        }

                        composable<Screen.OtpScreen> {
                            val viewModel: OtpViewModel = hiltViewModel()

                            LaunchedEffect(Unit) {
                                viewModel.effect.collect { effect ->
                                    when (effect) {
                                        is VerifyOtpEffect.Navigate -> {
                                            navigate(navController, effect.navigationAction)
                                        }

                                        is VerifyOtpEffect.ShowError -> {
                                            Logger.d("MainActivity", "Showing error")
                                            snackbarHostState.showSnackbar(
                                                message = effect.message,
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                }
                            }

                            OtpScreen(viewModel)
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