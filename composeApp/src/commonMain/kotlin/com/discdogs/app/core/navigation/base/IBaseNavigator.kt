package com.discdogs.app.core.navigation.base


import androidx.navigation.NavController


abstract class IBaseNavigator(protected val navController: NavController) : IViewNavigator {

    override fun navigateBack() {
        navController.popBackStack()
    }


    //This function allows you to navigate to route or return if its already on the backstack
    fun navigateAndPop(route: String) {
        val alreadyInBackStack = try {
            navController.getBackStackEntry(route)
            true
        } catch (e: IllegalArgumentException) {
            false
        }

        if (alreadyInBackStack) {
            navController.navigate(route) {
                popUpTo(route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        } else {
            navController.navigate(route) {
                launchSingleTop = true
            }
        }

    }

    fun navigateWithoutAddingToBackStack(route: String) {
        navController.navigate(route) {
            popUpTo(navController.currentBackStackEntry?.destination?.route ?: "") {
                inclusive = false
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }


}





