package com.discdogs.app.presentation.scan

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator


class ScanNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int, image: String) {
        navController.navigate(
            Route.ReleaseDetail(releaseId = id, image = image, source = "UNKNOWN")
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Scan> {

        }
    }
}