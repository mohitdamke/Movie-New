package com.example.mymovie.main.presentation.viewmodel

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymovie.R
import com.example.mymovie.common.BottomNavRoute
import com.example.mymovie.main.presentation.screen.MediaHomeScreen
import com.example.mymovie.main.presentation.screen.popularAndTvSeries.MediaListScreen
import com.example.mymovie.main.presentation.usecases.MainUiEvents
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.search.presentation.MediaSearchScreen
import com.example.mymovie.util.Constants


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun MediaMainScreen(
    navController: NavController, mainUiState: MainUiState, onEvent: (MainUiEvents) -> Unit
) {

    val items = listOf(
        BottomNavigationItem(
            title = "Movies",
            selectedIcon = Icons.Filled.Movie,
            unselectedIcon = Icons.Outlined.MovieFilter
        ), BottomNavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.SavedSearch,
            unselectedIcon = Icons.Outlined.Search,
        ), BottomNavigationItem(
            title = "Popular",
            selectedIcon = Icons.Filled.LocalFireDepartment,
            unselectedIcon = Icons.Outlined.LocalFireDepartment,
        ), BottomNavigationItem(
            title = "Tv Series",
            selectedIcon = Icons.Filled.LiveTv,
            unselectedIcon = Icons.Outlined.LiveTv
        )
    )

    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarNavController = rememberNavController()

    Scaffold(content = { paddingValues ->
        BottomNavigationScreens(
            selectedItem = selectedItem,
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()
            ),
            navController = navController,
            bottomBarNavController = bottomBarNavController,
            mainUiState = mainUiState,
            onEvent = onEvent
        )
    },

        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 16.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == index,
                        onClick = {
                            selectedItem.intValue = index

                            when (selectedItem.intValue) {
                                0 -> {
                                    bottomBarNavController.navigate(BottomNavRoute.MEDIA_HOME_SCREEN)
                                }

                                1 -> bottomBarNavController.navigate(
                                    BottomNavRoute.MEDIA_SEARCH_SCREEN
                                )

                                2 -> bottomBarNavController.navigate(
                                    "${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.popularScreen}"
                                )

                                3 -> bottomBarNavController.navigate(
                                    "${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.tvSeriesScreen}"
                                )
                            }

                        },
                        label = {

                            Text(
                                text = item.title,
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItem.intValue) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title, modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = colorResource(id = R.color.body),
                            unselectedTextColor = colorResource(id = R.color.body),
                            indicatorColor = MaterialTheme.colorScheme.background
                        ),
                    )
                }
            }
        })
}

@Composable
fun BottomNavigationScreens(
    selectedItem: MutableState<Int>,
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = bottomBarNavController,
        startDestination = BottomNavRoute.MEDIA_HOME_SCREEN
    ) {

        composable(BottomNavRoute.MEDIA_HOME_SCREEN) {
            MediaHomeScreen(
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }
        composable(BottomNavRoute.MEDIA_SEARCH_SCREEN) {
            MediaSearchScreen(
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
            )
        }

        composable(
            "${BottomNavRoute.MEDIA_LIST_SCREEN}?type={type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            MediaListScreen(
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                navBackStackEntry = navBackStackEntry,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

    }
}









