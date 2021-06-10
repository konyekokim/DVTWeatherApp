package com.konyekokim.core.network.responses

data class ForecastResp(
    val name: String,
    val cod: String,
    val country: String,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)