package com.example.mymovie.search.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mymovie.R
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.ui.theme.BigRadius
import com.example.mymovie.ui_shared_components.FocusedTopBar
import com.example.mymovie.util.Constants
import kotlin.math.roundToInt

@Composable
fun MediaSearchScreen(
    navController: NavController,
    mainUiState: MainUiState,
    bottomBarNavController: NavHostController,
) {

    val searchViewModel = hiltViewModel<SearchViewModel>()
    val searchScreenState = searchViewModel.searchScreenState.collectAsState().value

    val toolbarHeightPx = with(LocalDensity.current) { BigRadius.dp.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 30.dp)
            .nestedScroll(nestedScrollConnection)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {

            FocusedTopBar(
                toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
                searchScreenState = searchScreenState
            ) {
                searchViewModel.onEvent(SearchUiEvents.OnSearchQueryChanged(it))
            }


            if (searchScreenState.searchList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = BigRadius.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "No search results",
                        modifier = Modifier.size(300.dp).align(Alignment.Center)
                    )
                }
            } else {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(top = 10.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    items(searchScreenState.searchList.size) {
                        SearchMediaItem(
                            media = searchScreenState.searchList[it],
                            navController = navController,
                            mainUiState = mainUiState,
                            onEvent = searchViewModel::onEvent
                        )

                        if (it >= searchViewModel.searchScreenState.value.searchList.size - 1 && !mainUiState.isLoading) {
                            searchViewModel.onEvent(SearchUiEvents.OnPaginate(Constants.searchScreen))
                        }
                    }
                }



            }
        }
    }
}


