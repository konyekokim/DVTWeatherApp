package com.konyekokim.core.datasource.local

import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.database.CurrentWeatherDao
import com.konyekokim.core.database.ForecastWeatherDao
import javax.inject.Inject


class WeatherLocalDataSource @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastWeatherDao: ForecastWeatherDao
) {

    suspend fun saveCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherDao.saveCurrentWeather(currentWeather)
    }

    suspend fun saveForecastWeather(foreCastWeather: ForecastWeather) {
        forecastWeatherDao.saveForecastWeather(foreCastWeather)
    }

    suspend fun getLastSavedCurrentWeather() = currentWeatherDao.getLastSavedCurrentWeather()
    suspend fun getLastSavedForecastWeather() = forecastWeatherDao.getLastSavedForecastWeather()

}