package com.projach.comics_tracker.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    object RegisterUserScreen : Screen

    @Serializable
    data class LoginUserScreen(val username: String? = null) : Screen

    @Serializable
    data class AuthUserScreen(val email: String, val username: String) : Screen
}

sealed interface NavigationAction {
    data class NavigateTo(val screen: Screen) : NavigationAction
    data object PopBackStack : NavigationAction
    data class NavigateToAndClearBackStack(val screen: Screen) : NavigationAction
}


fun navigate(navController: NavController, navigationAction: NavigationAction) {
    when (navigationAction) {
        is NavigationAction.NavigateTo -> {
            navController.navigate(navigationAction.screen)
        }

        is NavigationAction.NavigateToAndClearBackStack -> {
            navController.navigate(navigationAction.screen) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }

        NavigationAction.PopBackStack -> {
            navController.popBackStack()
        }
    }
}
