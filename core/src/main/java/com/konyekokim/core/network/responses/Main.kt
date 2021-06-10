package com.konyekokim.core.network.responses

import com.squareup.moshi.Json

data class Main(
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    @field:Json(name = "temp_min") val tempMin: String,
    @field:Json(name = "temp_max") val tempMax: String
)