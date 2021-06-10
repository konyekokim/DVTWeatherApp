package com.konyekokim.weather.di

import com.konyekokim.commons.extensions.viewModel
import com.konyekokim.core.di.scopes.FeatureScope
import com.konyekokim.core.repository.WeatherRepository
import com.konyekokim.weather.WeatherFragment
import com.konyekokim.weather.WeatherViewModel
import dagger.Module
import dagger.Provides

@Module
class WeatherModule(private val weatherFragment: WeatherFragment) {

    @Provides
    @FeatureScope
    fun provideViewModel(
        weatherRepository: WeatherRepository
    ) = weatherFragment.viewModel {
        WeatherViewModel(weatherRepository)
    }

}