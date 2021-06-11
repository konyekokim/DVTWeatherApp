package com.konyekokim.dvtweatherapp

import android.app.Application
import com.konyekokim.core.di.CoreComponent
import com.konyekokim.core.di.DaggerCoreComponent
import com.konyekokim.core.di.modules.ContextModule
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.dvtweatherapp.di.DaggerAppComponent

class DvtWeatherApplication: Application(), CoreComponentProvider {

    lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        initDependencyInjection()
    }

    private fun initDependencyInjection(){
        coreComponent = DaggerCoreComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()

        DaggerAppComponent
            .builder()
            .coreComponent(coreComponent)
            .build()
            .inject(this)
    }

    override fun provideCoreComponent(): CoreComponent =
        coreComponent
}