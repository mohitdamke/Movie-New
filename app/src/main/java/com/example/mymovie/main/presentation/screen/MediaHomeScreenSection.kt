package com.example.mymovie.main.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mymovie.R
import com.example.mymovie.main.presentation.usecases.MainUiState
import com.example.mymovie.ui_shared_components.Item
import com.example.mymovie.util.Constants

@Composable
fun MediaHomeScreenSection(
    type: String,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
) {

    val title = when (type) {
        Constants.trendingAllListScreen -> {
            stringResource(id = R.string.trending)
        }

        Constants.tvSeriesScreen -> {
            stringResource(id = R.string.tv_series)
        }

        Constants.recommendedListScreen -> {
            stringResource(id = R.string.recommended)
        }

        else -> {
            stringResource(id = R.string.top_rated)
        }
    }

    val mediaList = when (type) {
        Constants.trendingAllListScreen -> {
            mainUiState.trendingAllList.take(10)
        }

        Constants.tvSeriesScreen -> {
            mainUiState.tvSeriesList.take(10)
        }

        Constants.recommendedListScreen -> {
            mainUiState.recommendedAllList.take(10)
        }

        else -> {
            mainUiState.topRatedAllList.take(10)
        }
    }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp
            )
        }

        LazyRow {
            items(mediaList.size) {

                var paddingEnd = 0.dp
                if (it == mediaList.size - 1) {
                    paddingEnd = 16.dp
                }

                Item(
                    media = mediaList[it],
                    navController = navController,
                    modifier = Modifier
                        .height(200.dp)
                        .width(150.dp)
                        .padding(start = 16.dp, end = paddingEnd)
                )
            }
        }
    }
}
