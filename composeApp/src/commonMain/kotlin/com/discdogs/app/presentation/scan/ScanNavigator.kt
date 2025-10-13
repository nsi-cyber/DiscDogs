package com.discdogs.app.presentation.scan

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel


class ScanNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int) {
        navController.navigate(
            Route.ReleaseDetail(releaseId = id, source = "SCAN")
        )
    }

    fun navigateToMasterDetail(masterId: Int, image: String?) {
        navController.navigate(
            Route.MasterDetail(masterId = masterId, image = image, source = "SCAN")
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Scan> {

            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)

            val viewModel = koinViewModel<ScanViewModel>()
            LaunchedEffect(Unit) {
                viewModel.process(ScanEvent.SetPermissionController(controller))
                viewModel.setNavigator(this@ScanNavigator)
            }
            ScanScreen(viewModel)
        }
    }
}