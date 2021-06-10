package com.konyekokim.core.datasource.remote

import com.konyekokim.core.data.Result
import com.konyekokim.core.network.responses.CurrentResp
import com.konyekokim.core.network.responses.ForecastResp
import com.konyekokim.core.network.services.WeatherService
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherService: WeatherService
) {

    suspend fun getCurrentWeatherByCity(city: String): Result<CurrentResp>{
        return try {
            val response = weatherService.getCurrentWeatherByCity(city)
            getCurrentWeatherResult(response = response, onError = {
                Result.Error("Error Fetching Current Weather by City ${response.code()} ${response.message()}")
            })
        } catch(e: Exception){
            Result.Error("Error fetching current weather by city")
        }
    }

    suspend fun getCurrentWeatherByCoordinates(lat: Double, lng: Double): Result<CurrentResp>{
        return try {
            val response = weatherService
                .getCurrentWeatherByCoordinates(
                    lat = lat,
                    lng = lng
                )
            getCurrentWeatherResult(response = response, onError = {
                Result.Error("Error Fetching Current Weather by Coordinates ${response.code()} ${response.message()}")
            })
        } catch(e: Exception){
            Result.Error("Error fetching current weather by coordinates")
        }
    }

    private inline fun getCurrentWeatherResult(
        response: Response<CurrentResp>,
        onError: () -> Result.Error
    ): Result<CurrentResp> {
        if(response.isSuccessful){
            val body = response.body()
            if(body != null){
                return Result.Success(body)
            }
        }
        return onError.invoke()
    }

    suspend fun getForecastByCity(city: String): Result<ForecastResp>{
        return try {
            val response = weatherService.getForecastByCity(city)
            getForecastResult(response = response, onError = {
                Result.Error("Error Fetching Forecast by City ${response.code()} ${response.message()}")
            })
        } catch(e: Exception){
            Result.Error("Error fetching forecast by city")
        }
    }

    suspend fun getForecastByCoordinates(lat: Double, lng: Double): Result<ForecastResp>{
        return try {
            val response = weatherService
                .getForecastByCoordinates(
                    lat = lat,
                    lng = lng
                )
            getForecastResult(response = response, onError = {
                Result.Error("Error Fetching Forecast by Coordinates ${response.code()} ${response.message()}")
            })
        } catch(e: Exception){
            Result.Error("Error fetching forecast by coordinates")
        }
    }

    private inline fun getForecastResult(
        response: Response<ForecastResp>,
        onError: () -> Result.Error
    ): Result<ForecastResp> {
        if(response.isSuccessful){
            val body = response.body()
            if(body != null){
                return Result.Success(body)
            }
        }
        return onError.invoke()
    }
}