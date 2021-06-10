package com.konyekokim.weather

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.konyekokim.commons.extensions.appContext
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.weather.di.WeatherModule
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    @Inject
    lateinit var viewModel: WeatherViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUpDependencyInjection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpDependencyInjection(){
       /* DaggerWeatherComponent
            .builder()
            .coreComponent((appContext as CoreComponentProvider).provideCoreComponent())
            .weatherModule(WeatherModule(this))
            .build()
            .inject(this)*/
    }
}