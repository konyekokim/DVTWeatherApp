package com.konyekokim.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.konyekokim.core.network.responses.City
import com.konyekokim.core.network.responses.WeatherData

@Entity(tableName = "forecast_weather")
class ForecastWeather(
    @PrimaryKey
    val ids: Int = 1,
    val name: String,
    val cod: String,
    val country: String,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)