package com.discdogs.app.presentation.search

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import com.discdogs.app.presentation.releaseDetail.DetailSource
import org.koin.compose.viewmodel.koinViewModel

class SearchNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToMasterDetail(id: Int, image: String) {
        navController.navigate(
            Route.MasterDetail(
                id, image, DetailSource.SEARCH.name
            )
        )
    }

    fun navigateToReleaseDetail(id: Int, image: String) {
        navController.navigate(
            Route.ReleaseDetail(
                id, image, DetailSource.SEARCH.name
            )
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Search> {
            val viewModel = koinViewModel<SearchViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@SearchNavigator)
            }
            SearchScreen(viewModel)
        }
    }
}