package com.konyekokim.core.database

import androidx.room.Database
import com.konyekokim.core.BuildConfig
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.ForecastWeather

@Database(
    entities = [CurrentWeather::class, ForecastWeather::class],
    exportSchema = false,
    version = BuildConfig.WEATHER_DATABASE_VERSION
)

abstract class WeatherDatabase {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
}