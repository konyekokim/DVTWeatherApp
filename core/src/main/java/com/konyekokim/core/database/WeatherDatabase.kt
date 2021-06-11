package com.konyekokim.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.konyekokim.core.BuildConfig
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather

@Database(
    entities = [CurrentWeather::class, ForecastWeather::class, FavoriteLocation::class],
    exportSchema = false,
    version = BuildConfig.WEATHER_DATABASE_VERSION
)
@TypeConverters(Converter::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
    abstract fun favoriteLocationDao(): FavoriteLocationDao
}