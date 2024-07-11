package com.example.mymovie.onboarding

import androidx.annotation.DrawableRes
import com.example.mymovie.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)


val pages = listOf(
    Page(
        title = "Welcome to MovieHub",
        description = "Discover and bookmark your favorite movies. Get personalized recommendations and never miss out on the latest releases.",
        image = R.drawable.onboarding1
    ),

    Page(
        title = "Discover New Movies",
        description = "Browse through our extensive movie library. Find movies by genre, rating, and more. Your next favorite movie is just a tap away!",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Watch and Enjoy",
        description = "Save movies to your watchlist. Access them anytime, anywhere, and stay updated with your personalized movie collection.",
        image = R.drawable.onboarding3
    )

)