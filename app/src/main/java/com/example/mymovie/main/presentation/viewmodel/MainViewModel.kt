package com.example.mymovie.main.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.main.domain.repository.GenreRepository
import com.example.mymovie.main.domain.repository.MediaRepository
import com.example.mymovie.main.presentation.usecases.MainUiEvents
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.nav.Route
import com.example.mymovie.onboarding.app_entry.AppEntryUseCases
import com.example.mymovie.util.Constants
import com.example.mymovie.util.Constants.ALL
import com.example.mymovie.util.Constants.MOVIE
import com.example.mymovie.util.Constants.NOW_PLAYING
import com.example.mymovie.util.Constants.POPULAR
import com.example.mymovie.util.Constants.TOP_RATED
import com.example.mymovie.util.Constants.TRENDING_TIME
import com.example.mymovie.util.Constants.TV
import com.example.mymovie.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val genreRepository: GenreRepository,
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {


    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    var splashCondition by mutableStateOf(true)
        private set


    var startDestination by mutableStateOf(Route.ONBOARDING_SCREEN)
        private set

    init {
        appEntryUseCases.readAppEntry().onEach { shouldStartFromHomeScreen ->
            if (shouldStartFromHomeScreen) {
                startDestination = Route.MEDIA_MAIN_SCREEN
            } else {
                startDestination = Route.ONBOARDING_SCREEN
            }
            delay(500)
            splashCondition = false
        }.launchIn(viewModelScope)
        load()
    }

    private fun load(fetchFromRemote: Boolean = false) {
        loadPopularMovies(fetchFromRemote)
        loadTopRatedMovies(fetchFromRemote)
        loadNowPlayingMovies(fetchFromRemote)

        loadTopRatedTvSeries(fetchFromRemote)
        loadPopularTvSeries(fetchFromRemote)

        loadTrendingAll(fetchFromRemote)

        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = true
        )
        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = false
        )
    }


    fun onEvent(event: MainUiEvents) {
        when (event) {

            is MainUiEvents.Refresh -> {

                _mainUiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = true
                )

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = false
                )

                when (event.type) {

                    Constants.homeScreen -> {

                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.popularScreen -> {
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                }
            }

            is MainUiEvents.OnPaginate -> {

                when (event.type) {

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(true)
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(true)
                        loadTopRatedTvSeries(true)

                        loadPopularTvSeries(true)
                    }

                    Constants.popularScreen -> {

                        Timber.tag(Constants.GET_TAG).d("onOnPaginate: popularScreen")
                        loadPopularMovies(true)
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(true)
                        loadTopRatedTvSeries(true)
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(true)
                        loadTopRatedTvSeries(true)
                    }

                }
            }
        }
    }

    private fun loadGenres(
        fetchFromRemote: Boolean,
        isMovies: Boolean
    ) {
        viewModelScope.launch {

            if (isMovies) {
                genreRepository
                    .getGenres(fetchFromRemote, MOVIE)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    _mainUiState.update {
                                        it.copy(
                                            moviesGenresList = genresList
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            } else {
                genreRepository
                    .getGenres(fetchFromRemote, TV)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    _mainUiState.update {
                                        it.copy(
                                            tvGenresList = genresList
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            }


        }
    }


    private fun loadPopularMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    POPULAR,
                    mainUiState.value.popularMoviesPage,
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = shuffledMediaList.toList(),
                                            popularMoviesPage = 1
                                        )
                                    }
                                } else {

                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList =
                                            mainUiState.value.popularMoviesList + shuffledMediaList.toList(),
                                            popularMoviesPage = mainUiState.value.popularMoviesPage + 1
                                        )
                                    }
                                }

                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadTopRatedMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    TOP_RATED,
                    mainUiState.value.topRatedMoviesPage,

                    )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = shuffledMediaList.toList(),
                                            topRatedMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList =
                                            mainUiState.value.topRatedMoviesList + shuffledMediaList.toList(),
                                            topRatedMoviesPage = mainUiState.value.topRatedMoviesPage + 1
                                        )
                                    }
                                }

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadNowPlayingMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    NOW_PLAYING,
                    mainUiState.value.nowPlayingMoviesPage,

                    )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList =
                                            mainUiState.value.nowPlayingMoviesList + shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = mainUiState.value.nowPlayingMoviesPage + 1
                                        )
                                    }
                                }

                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTopRatedTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    TOP_RATED,
                    mainUiState.value.topRatedTvSeriesPage,

                    )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList =
                                            mainUiState.value.topRatedTvSeriesList + shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = mainUiState.value.topRatedTvSeriesPage + 1
                                        )
                                    }
                                }


                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadPopularTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    POPULAR,
                    mainUiState.value.popularTvSeriesPage,

                    )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = shuffledMediaList.toList(),
                                            popularTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList =
                                            mainUiState.value.popularTvSeriesList + shuffledMediaList.toList(),
                                            popularTvSeriesPage = mainUiState.value.popularTvSeriesPage + 1
                                        )
                                    }
                                }


                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTrendingAll(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getTrendingList(
                    fetchFromRemote,
                    isRefresh,
                    ALL,
                    TRENDING_TIME,
                    mainUiState.value.trendingAllPage,

                    )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = shuffledMediaList.toList(),
                                            trendingAllPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList =
                                            mainUiState.value.trendingAllList + shuffledMediaList.toList(),
                                            trendingAllPage = mainUiState.value.trendingAllPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun createSpecialList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ) {

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    specialList = emptyList()
                )
            }
        }

        if (mainUiState.value.specialList.size >= 7) {
            return
        }


        val shuffledMediaList = mediaList.take(7).toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    specialList = shuffledMediaList
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    specialList = mainUiState.value.specialList + shuffledMediaList
                )
            }

        }

        for (item in mainUiState.value.specialList) {
            Timber.tag("special_list").d(item.title)
        }
    }

    private fun createTvSeriesList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    tvSeriesList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    tvSeriesList = mainUiState.value.tvSeriesList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createTopRatedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    topRatedAllList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    topRatedAllList = mainUiState.value.topRatedAllList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createRecommendedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    recommendedAllList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    recommendedAllList = mainUiState.value.recommendedAllList + shuffledMediaList.toList()
                )
            }
        }

        createSpecialList(
            mediaList = mediaList,
            isRefresh = isRefresh
        )
    }
}