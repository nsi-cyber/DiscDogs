package com.discdogs.app.presentation.releaseDetail

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.discdogs.app.app.Route
import com.discdogs.app.core.navigation.base.IBaseNavigator
import org.koin.compose.viewmodel.koinViewModel

class ReleaseDetailNavigator(
    navController: NavHostController
) : IBaseNavigator(navController) {


    fun navigateToMastersVersions(masterId: Int) {
        navController.navigate(
            Route.ReleaseVersions(
                masterId
            )
        )
    }

    override fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable<Route.ReleaseDetail> {

            val uriHandler = LocalUriHandler.current
            val viewModel: ReleaseDetailViewModel = koinViewModel<ReleaseDetailViewModel>()
            LaunchedEffect(Unit) {
                viewModel.setNavigator(this@ReleaseDetailNavigator)
                viewModel.setUriHandler(uriHandler)
            }
            ReleaseDetailScreen(viewModel)

        }
    }
}

enum class DetailSource {
    SEARCH, SCAN, UNKNOWN
}

fun String.toDetailSource(): DetailSource {
    return try {
        DetailSource.valueOf(this)
    } catch (e: Exception) {
        DetailSource.UNKNOWN
    }
}