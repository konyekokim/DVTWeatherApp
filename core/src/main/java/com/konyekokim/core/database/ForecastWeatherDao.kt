package com.konyekokim.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konyekokim.core.data.entities.ForecastWeather

@Dao
interface ForecastWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecastWeather(forecastWeather: ForecastWeather)

    @Query("SELECT * FROM forecast_weather WHERE ids = 1")
    suspend fun getLastSavedForecastWeather(): ForecastWeather?

}