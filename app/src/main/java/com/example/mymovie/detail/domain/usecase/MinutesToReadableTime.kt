package com.example.mymovie.detail.domain.usecase

import java.util.Locale

class MinutesToReadableTime(
    private val minutes: Int
) {
    operator fun invoke(): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format(Locale.getDefault(), "%02d hr %02d min", hours, remainingMinutes)
    }
}
