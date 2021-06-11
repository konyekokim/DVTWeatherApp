package com.konyekokim.core.network.responses

data class CurrentResp(
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val id: Int,
    val name: String,
    val cod: Int,
    val sys: Sys,
    val coord: Coordinates
)