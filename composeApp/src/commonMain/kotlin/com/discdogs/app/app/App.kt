package com.discdogs.app.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.discdogs.app.core.presentation.AppLevelSnackbar
import com.discdogs.app.core.presentation.UiText
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.VEBottomNavigationBar
import com.discdogs.app.presentation.components.VEBottomNavigationItem
import com.discdogs.app.presentation.detail.DetailNavigator
import com.discdogs.app.presentation.library.LibraryNavigator
import com.discdogs.app.presentation.listdetail.ListDetailNavigator
import com.discdogs.app.presentation.releases.ReleasesNavigator
import com.discdogs.app.presentation.scan.ScanNavigator
import com.discdogs.app.presentation.search.SearchNavigator
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.ic_library
import discdog.composeapp.generated.resources.ic_scan
import discdog.composeapp.generated.resources.ic_search
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun App() {
    VETheme {
        val rootNavController = rememberNavController()
        val innerNavController = rememberNavController()

        val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination?.route.toString()

        val bottomBarList: List<VEBottomNavigationItem> = listOf(
            VEBottomNavigationItem(
                Route.Search,
                UiText.DynamicString("Search"),
                Res.drawable.ic_search,
            ),
            VEBottomNavigationItem(
                Route.Scan,
                UiText.DynamicString("Scan"),
                Res.drawable.ic_scan,
            ),
            VEBottomNavigationItem(
                Route.Favorites,
                UiText.DynamicString("Library"),
                Res.drawable.ic_library,
            ),


            )

        Scaffold {

            NavHost(
                navController = rootNavController,
                startDestination = Route.MainScreen
            ) {
                ListDetailNavigator(rootNavController).build(this)
                ReleasesNavigator(rootNavController).build(this)
                DetailNavigator(rootNavController).build(this)
                composable<Route.MainScreen> {

                    Scaffold(
                        containerColor = VETheme.colors.backgroundColorPrimary, bottomBar = {

                            VEBottomNavigationBar(
                                items = bottomBarList,
                                currentRoute = currentDestination,
                                navigateTo = { selectedTab ->
                                    innerNavController.navigate(selectedTab)
                                },
                                returnToStart = {
                                    innerNavController.clearBackStack(it)
                                })

                        },
                        content = {
                            NavHost(
                                navController = innerNavController,
                                startDestination = Route.Search
                            ) {
                                SearchNavigator(rootNavController).build(this)
                                ScanNavigator(rootNavController).build(this)
                                LibraryNavigator(rootNavController).build(this)
                            }
                        })
                }
            }

            AppLevelSnackbar(modifier = Modifier.padding(top = it.calculateTopPadding()))

        }

    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}

fun NavController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination()) {
            saveState = true
        }
    }
}