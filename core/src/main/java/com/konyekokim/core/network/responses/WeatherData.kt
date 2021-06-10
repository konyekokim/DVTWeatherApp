package com.konyekokim.core.network.responses

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val id: Int,
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val rain: Rain,
    @SerializedName("dt_txt") val dtTxt: String?
)