package com.discdogs.app.presentation.listdetail

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.app.Route.ReleaseDetail
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class ListDetailNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int, image: String) {
        navController.navigate(
            ReleaseDetail(
                id, image, "UNKNOWN"
            )
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.FavoriteList> { backStackEntry ->
            val viewModel = koinViewModel<ListDetailViewModel>()

            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@ListDetailNavigator)
            }

            ListDetailScreen(viewModel)
        }
    }
}
