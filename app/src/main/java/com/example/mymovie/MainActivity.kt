package com.example.mymovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymovie.detail.presentation.similar_media.SimilarMediaListScreen
import com.ahmedapps.themovies.media_details.presentation.watch_video.WatchVideoScreen
import com.example.mymovie.detail.presentation.MediaDetailScreen
import com.example.mymovie.detail.presentation.MediaDetailsScreenEvents
import com.example.mymovie.detail.presentation.MediaDetailsViewModel
import com.example.mymovie.detail.presentation.SomethingWentWrong
import com.example.mymovie.main.presentation.usecases.MainUiEvents
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.main.presentation.viewmodel.MainViewModel
import com.example.mymovie.main.presentation.viewmodel.MediaMainScreen
import com.example.mymovie.nav.Route
import com.example.mymovie.onboarding.OnBoardingViewModel
import com.example.mymovie.onboarding.components.OnBoardingScreen
import com.example.mymovie.ui.theme.MyMovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getActionBar()?.hide();
        setContent {
            MyMovieTheme {

                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainUiState = mainViewModel.mainUiState.collectAsState().value


                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        mainViewModel.splashCondition
                    }
                }
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    val startDestination = mainViewModel.startDestination
                    Navigation(
                        mainUiState = mainUiState,
                        onEvent = mainViewModel::onEvent,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}


@Composable
fun Navigation(
    mainUiState: MainUiState,
    startDestination: String,
    onEvent: (MainUiEvents) -> Unit
) {
    val navController = rememberNavController()

    val mediaDetailsViewModel = hiltViewModel<MediaDetailsViewModel>()
    val mediaDetailsScreenState =
        mediaDetailsViewModel.mediaDetailsScreenState.collectAsState().value

    val viewModel: OnBoardingViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        composable(Route.ONBOARDING_SCREEN) {
            OnBoardingScreen(event = viewModel::onEvent)
        }


        composable(Route.MEDIA_MAIN_SCREEN) {
            MediaMainScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(
            "${Route.MEDIA_DETAILS_SCREEN}?id={id}&type={type}&category={category}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) {

            val id = it.arguments?.getInt("id") ?: 0
            val type = it.arguments?.getString("type") ?: ""
            val category = it.arguments?.getString("category") ?: ""

            LaunchedEffect(key1 = true) {
                mediaDetailsViewModel.onEvent(
                    MediaDetailsScreenEvents.SetDataAndLoad(
                        moviesGenresList = mainUiState.moviesGenresList,
                        tvGenresList = mainUiState.tvGenresList,
                        id = id,
                        type = type,
                        category = category
                    )
                )
            }

            if (mediaDetailsScreenState.media != null) {
                MediaDetailScreen(
                    navController = navController,
                    media = mediaDetailsScreenState.media,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    onEvent = mediaDetailsViewModel::onEvent
                )
            } else {
                SomethingWentWrong()
            }
        }

        composable(
            "${Route.SIMILAR_MEDIA_LIST_SCREEN}?title={title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
            )
        ) {

            val name = it.arguments?.getString("title") ?: ""

            SimilarMediaListScreen(
                navController = navController,
                mediaDetailsScreenState = mediaDetailsScreenState,
                name = name,
            )
        }

        composable(
            "${Route.WATCH_VIDEO_SCREEN}?videoId={videoId}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ) {

            val videoId = it.arguments?.getString("videoId") ?: ""

            WatchVideoScreen(
                lifecycleOwner = LocalLifecycleOwner.current,
                videoId = videoId
            )
        }
    }
}




















