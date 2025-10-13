package com.discdogs.app.presentation.releases

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class ReleasesNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int, image: String, sourceType: String) {
        navController.navigate(
            Route.ReleaseDetail(releaseId = id, image = image, source = sourceType)
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.ReleaseVersions> {
            val viewModel = koinViewModel<ReleasesViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@ReleasesNavigator)
            }
            ReleasesScreen(viewModel)
        }
    }
}