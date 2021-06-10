package com.konyekokim.core.network.responses

import com.squareup.moshi.Json

data class WeatherData(
    val id: Int,
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val rain: Rain,
    @field:Json(name = "dt_txt") val dtTxt: String?
)