package com.konyekokim.core.repository

import com.konyekokim.core.data.Result
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.datasource.local.WeatherLocalDataSource
import com.konyekokim.core.datasource.remote.WeatherRemoteDataSource
import com.konyekokim.core.mappers.CurrentRespToCurrentWeatherMapper
import com.konyekokim.core.mappers.ForecastRespToForecastWeatherMapper
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val currentWeatherMapper: CurrentRespToCurrentWeatherMapper,
    private val forecastWeatherMapper: ForecastRespToForecastWeatherMapper
) {

    suspend fun getCurrentWeatherByCity(city: String): Result<CurrentWeather>{
        return when (val response = remoteDataSource.getCurrentWeatherByCity(city)){
            is Result.Success -> {
                val currentWeather = currentWeatherMapper.map(response.data)
                localDataSource.saveCurrentWeather(currentWeather)
                Result.Success(currentWeather)
            }
            is Result.Error -> response
        }
    }

    suspend fun getCurrentWeatherByCoordinates(lat: Double, lng: Double): Result<CurrentWeather>{
        return when (val response = remoteDataSource
            .getCurrentWeatherByCoordinates(lat = lat, lng = lng)){
            is Result.Success -> {
                val currentWeather = currentWeatherMapper.map(response.data)
                localDataSource.saveCurrentWeather(currentWeather)
                Result.Success(currentWeather)
            }
            is Result.Error -> response
        }
    }

    suspend fun getLastSavedCurrentWeather() = localDataSource.getLastSavedCurrentWeather()

    suspend fun getForecastByCity(city: String): Result<ForecastWeather>{
        return when (val response = remoteDataSource.getForecastByCity(city)){
            is Result.Success -> {
                val forecastWeather = forecastWeatherMapper.map(response.data)
                localDataSource.saveForecastWeather(forecastWeather)
                Result.Success(forecastWeather)
            }
            is Result.Error -> response
        }
    }

    suspend fun getForecastByCoordinates(lat: Double, lng: Double): Result<ForecastWeather>{
        return when (val response = remoteDataSource
            .getForecastByCoordinates(lat = lat, lng = lng)){
            is Result.Success -> {
                val forecastWeather = forecastWeatherMapper.map(response.data)
                localDataSource.saveForecastWeather(forecastWeather)
                Result.Success(forecastWeather)
            }
            is Result.Error -> response
        }
    }

    suspend fun getLastSavedForecastWeather() = localDataSource.getLastSavedForecastWeather()
}