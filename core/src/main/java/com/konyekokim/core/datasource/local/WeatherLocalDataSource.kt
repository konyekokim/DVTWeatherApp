package com.konyekokim.core.datasource.local

import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.database.CurrentWeatherDao
import com.konyekokim.core.database.FavoriteLocationDao
import com.konyekokim.core.database.ForecastWeatherDao
import javax.inject.Inject


class WeatherLocalDataSource @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastWeatherDao: ForecastWeatherDao,
    private val favoriteLocationDao: FavoriteLocationDao
) {

    suspend fun saveCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherDao.saveCurrentWeather(currentWeather)
    }

    suspend fun saveForecastWeather(foreCastWeather: ForecastWeather) {
        forecastWeatherDao.saveForecastWeather(foreCastWeather)
    }

    suspend fun getLastSavedCurrentWeather() = currentWeatherDao.getLastSavedCurrentWeather()
    suspend fun getLastSavedForecastWeather() = forecastWeatherDao.getLastSavedForecastWeather()

    suspend fun saveFavoriteLocation(favoriteLocation: FavoriteLocation) {
        favoriteLocationDao.saveLocation(favoriteLocation)
    }

    suspend fun getFavoriteLocations() = favoriteLocationDao.getFavoriteLocations()

    suspend fun deleteFavoriteLocation(cityName: String){
        favoriteLocationDao.deleteFavoriteLocation(cityName)
    }

}