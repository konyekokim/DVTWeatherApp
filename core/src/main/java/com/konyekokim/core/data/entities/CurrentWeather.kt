package com.konyekokim.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.konyekokim.core.network.responses.*

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey
    val ids: Int = 1,
    val id: Int,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val name: String,
    val cod: Int,
    val sys: Sys,
    val coord: Coordinates
)