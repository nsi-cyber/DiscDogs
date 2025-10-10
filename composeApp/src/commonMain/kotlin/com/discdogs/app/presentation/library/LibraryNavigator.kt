package com.discdogs.app.presentation.library

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class LibraryNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToReleaseDetail(id: Int, image: String) {


    }

    fun navigateToListDetail(listId: Long) {

    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.Favorites> {
            val viewModel = koinViewModel<LibraryViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@LibraryNavigator)
            }
            LibraryScreen(viewModel)
        }
    }
}