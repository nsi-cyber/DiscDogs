package com.discdogs.app.presentation.onboarding

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class OnboardingNavigator(
    navController: NavHostController,
) : IBaseNavigator(navController) {


    fun navigateToMainScreen() {
        navController.navigate(Route.MainScreen) {
            popUpTo(0)
            launchSingleTop = true
            restoreState = false
        }
    }


    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Onboarding> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@OnboardingNavigator)
            }
            OnboardingScreen(
                viewModel = viewModel
            )
        }
    }
}