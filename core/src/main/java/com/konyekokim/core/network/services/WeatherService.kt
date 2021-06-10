package com.konyekokim.core.network.services

import com.konyekokim.core.network.responses.CurrentResp
import com.konyekokim.core.network.responses.ForecastResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{
    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query("q")city: String
    ): Response<CurrentResp>

    @GET("weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): Response<CurrentResp>

    @GET("forecast")
    suspend fun getForecastByCity(
        @Query("q") city: String
    ): Response<ForecastResp>

    @GET("forecast")
    suspend fun getForecastByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double
    ): Response<ForecastResp>
}