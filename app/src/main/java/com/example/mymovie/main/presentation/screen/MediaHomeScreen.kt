package com.example.mymovie.main.presentation.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mymovie.R
import com.example.mymovie.common.BottomNavRoute
import com.example.mymovie.main.presentation.usecases.MainUiEvents
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.topbar.HomeTopBar
import com.example.mymovie.ui.theme.MediumRadius
import com.example.mymovie.ui_shared_components.AutoSwipeSection
import com.example.mymovie.ui_shared_components.ShouldShowMediaHomeScreenSectionOrShimmer
import com.example.mymovie.ui_shared_components.shimmerEffect
import com.example.mymovie.util.Constants
import com.example.mymovie.util.Constants.recommendedListScreen
import com.example.mymovie.util.Constants.topRatedAllListScreen
import com.example.mymovie.util.Constants.trendingAllListScreen
import com.example.mymovie.util.Constants.tvSeriesScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaHomeScreen(
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {

    val context = LocalContext.current
    BackHandler(
        enabled = true
    ) {
        (context as Activity).finish()
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)

        onEvent(MainUiEvents.Refresh(type = Constants.homeScreen))

        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
        ) {
            HomeTopBar(onSearchClick =
            { bottomBarNavController.navigate(BottomNavRoute.MEDIA_SEARCH_SCREEN) })

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = trendingAllListScreen,
                showShimmer = mainUiState.trendingAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            if (mainUiState.specialList.isEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(id = R.string.special),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth(0.8f)
                        .padding(
                            top = 20.dp, bottom = 12.dp
                        )
                        .clip(RoundedCornerShape(MediumRadius))
                        .shimmerEffect(false)
                        .align(CenterHorizontally)
                )
            } else {
                AutoSwipeSection(
                    type = stringResource(id = R.string.special),
                    navController = navController,
                    mainUiState = mainUiState
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = tvSeriesScreen,
                showShimmer = mainUiState.tvSeriesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = topRatedAllListScreen,
                showShimmer = mainUiState.topRatedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = recommendedListScreen,
                showShimmer = mainUiState.recommendedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))
        }

        PullRefreshIndicator(
            refreshing, refreshState,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = (10 - 1).dp)
        )
    }

}



