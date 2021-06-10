package com.konyekokim.weather.di

import com.konyekokim.core.di.CoreComponent
import com.konyekokim.core.di.scopes.FeatureScope
import com.konyekokim.weather.WeatherFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [WeatherModule::class],
    dependencies = [CoreComponent::class]
)
interface WeatherComponent {
    fun inject(weatherFragment: WeatherFragment)
}