package com.discdogs.app.presentation.search

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.app.Route.ReleaseDetail
import com.discdogs.app.core.navigation.base.IBaseNavigator
import com.discdogs.app.presentation.detail.DetailSource
import org.koin.compose.viewmodel.koinViewModel

class SearchNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int, image: String) {
        navController.navigate(
            ReleaseDetail(
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