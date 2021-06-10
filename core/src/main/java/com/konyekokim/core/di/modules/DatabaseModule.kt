package com.konyekokim.core.di.modules

import android.content.Context
import androidx.room.Room
import com.konyekokim.core.BuildConfig
import com.konyekokim.core.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            BuildConfig.WEATHER_DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.currentWeatherDao()

    @Provides
    @Singleton
    fun provideForecastWeatherDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.forecastWeatherDao()

}