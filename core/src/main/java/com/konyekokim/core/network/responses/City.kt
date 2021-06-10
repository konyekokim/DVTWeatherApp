package com.konyekokim.core.network.responses

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coordinates
)