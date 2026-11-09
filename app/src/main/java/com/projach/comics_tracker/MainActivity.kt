package com.projach.comics_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.navigation.navigate
import com.projach.comics_tracker.ui.screens.comicsList.ComicsScreen
import com.projach.comics_tracker.ui.screens.comicsList.ComicsViewModel
import com.projach.comics_tracker.ui.screens.login.LoginUserEffect
import com.projach.comics_tracker.ui.screens.login.LoginUserScreen
import com.projach.comics_tracker.ui.screens.login.LoginUserViewModel
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
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition { viewModel.startDestination.value == null }

        enableEdgeToEdge()

        setContent {
            val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

            startDestination?.let { destination ->
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    viewModel.unauthorizedEvent.collect {
                        navController.navigate(Screen.LoginUserScreen()) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }

                val snackbarHostState = remember { SnackbarHostState() }

                ComicsTrackerTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = destination,
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
                                val viewModel: LoginUserViewModel = hiltViewModel()

                                LaunchedEffect(Unit) {
                                    viewModel.effect.collect { effect ->
                                        when (effect) {
                                            is LoginUserEffect.ShowError -> {
                                                Logger.d("MainActivity", "Showing error")
                                                snackbarHostState.showSnackbar(
                                                    message = effect.message,
                                                    duration = SnackbarDuration.Short,
                                                    withDismissAction = true
                                                )
                                            }

                                            is LoginUserEffect.Navigate -> {
                                                navigate(
                                                    navController = navController,
                                                    navigationAction = effect.navigation
                                                )
                                            }
                                        }
                                    }
                                }

                                LoginUserScreen(viewModel)
                            }

                            composable<Screen.ComicsListScreen> {
                                val viewModel: ComicsViewModel = hiltViewModel()
                                ComicsScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}