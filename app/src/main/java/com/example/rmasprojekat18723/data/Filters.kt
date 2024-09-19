package com.example.rmasprojekat18723.data

data class Filters(
    val author: String,
    val ratingFrom: Int?,
    val ratingTo: Int?,
    val startDate: Long?,
    val endDate: Long?,
    val durationFrom: Float?,
    val durationTo: Float?,
    val radius: Float?
)