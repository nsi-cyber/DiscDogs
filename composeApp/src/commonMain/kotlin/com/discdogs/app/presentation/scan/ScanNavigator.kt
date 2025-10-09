package com.discdogs.app.presentation.scan

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel


class ScanNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int) {
        navController.navigate(
            Route.ReleaseDetail(releaseId = id, source = "SCAN")
        )
    }

    fun navigateToReleaseVersions(masterId: Int) {
        navController.navigate(
            Route.ReleaseVersions(masterId = masterId)
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Scan> {

            val viewModel = koinViewModel<ScanViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@ScanNavigator)
            }
            ScanScreen(viewModel)
        }
    }
}