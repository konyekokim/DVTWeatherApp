package com.konyekokim.dvtweatherapp.di

import android.content.Context
import com.konyekokim.dvtweatherapp.DvtWeatherApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: DvtWeatherApplication): Context =
        application.applicationContext

}