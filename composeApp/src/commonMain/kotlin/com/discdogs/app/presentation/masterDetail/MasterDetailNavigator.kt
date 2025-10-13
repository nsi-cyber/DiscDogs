package com.discdogs.app.presentation.masterDetail

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class MasterDetailNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToMastersVersions(masterId: Int, sourceType: String) {
        navController.navigate(
            Route.ReleaseVersions(
                masterId, sourceType
            )
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.MasterDetail> {

            val uriHandler = LocalUriHandler.current
            val viewModel: MasterDetailViewModel = koinViewModel<MasterDetailViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@MasterDetailNavigator)
                viewModel.setUriHandler(uriHandler)
            }
            MasterDetailScreen(viewModel)

        }
    }
}



