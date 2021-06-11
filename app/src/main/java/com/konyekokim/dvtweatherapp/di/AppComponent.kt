package com.konyekokim.dvtweatherapp.di

import com.konyekokim.core.di.CoreComponent
import com.konyekokim.core.di.scopes.AppScope
import com.konyekokim.dvtweatherapp.DvtWeatherApplication
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class]
)
interface AppComponent {
    fun inject(application: DvtWeatherApplication)
}