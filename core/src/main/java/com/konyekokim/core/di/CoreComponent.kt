package com.konyekokim.core.di

import android.content.Context
import com.konyekokim.core.data.AppCoroutineDispatchers
import com.konyekokim.core.database.CurrentWeatherDao
import com.konyekokim.core.database.FavoriteLocationDao
import com.konyekokim.core.database.ForecastWeatherDao
import com.konyekokim.core.di.modules.ContextModule
import com.konyekokim.core.di.modules.DatabaseModule
import com.konyekokim.core.di.modules.DispatcherModule
import com.konyekokim.core.di.modules.NetworkModule
import com.konyekokim.core.network.services.WeatherService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        DispatcherModule::class
    ]
)
interface CoreComponent {
    fun context(): Context
    fun weatherService(): WeatherService
    fun currentWeatherDao(): CurrentWeatherDao
    fun forecastWeatherDao(): ForecastWeatherDao
    fun favoriteLocationDao(): FavoriteLocationDao
    fun coroutineDispatchers(): AppCoroutineDispatchers
}