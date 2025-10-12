package com.discdogs.app.presentation.splash

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class SplashNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToOnboardingScreen() {
        navController.navigate(Route.Onboarding) {
            popUpTo(0)
            launchSingleTop = true
            restoreState = false
        }
    }

    fun navigateToMainScreen() {
        navController.navigate(Route.MainScreen) {
            popUpTo(0)
            launchSingleTop = true
            restoreState = false
        }
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Splash> {
            val viewModel = koinViewModel<SplashViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@SplashNavigator)
            }

            SplashScreen(viewModel)
        }
    }
}