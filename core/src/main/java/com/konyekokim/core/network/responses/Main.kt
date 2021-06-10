package com.konyekokim.core.network.responses

import com.google.gson.annotations.SerializedName


data class Main(
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("temp_min") val tempMin: String,
    @SerializedName("temp_max") val tempMax: String
)