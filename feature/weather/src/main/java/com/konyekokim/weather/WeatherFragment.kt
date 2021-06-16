package com.konyekokim.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.konyekokim.commons.extensions.*
import com.konyekokim.commons.ui.getDateString
import com.konyekokim.commons.utils.PermissionUtils
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.weather.adapter.ForecastAdapter
import com.konyekokim.weather.databinding.FragmentWeatherBinding
import com.konyekokim.weather.di.DaggerWeatherComponent
import com.konyekokim.weather.di.WeatherModule
import com.konyekokim.weather.utils.checkConnectivity
import com.konyekokim.weather.utils.prepareForecastData
import com.konyekokim.weather.utils.showFavoriteCityDialogs
import java.util.*
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    @Inject
    lateinit var viewModel: WeatherViewModel

    private lateinit var binding: FragmentWeatherBinding

    private lateinit var forecastAdapter: ForecastAdapter

    private var currentLocation: FavoriteLocation? = null
    private var mCurrentWeather: CurrentWeather? = null
    private var favoriteCites: List<FavoriteLocation> = ArrayList()

    var lat = 0.00
    var lon = 0.00
    var count = 0
    private var justLaunched = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUpDependencyInjection()
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(requireActivity()) -> {
                when {
                    PermissionUtils.isLocationEnabled(requireActivity()) -> {
                        requireContext().checkConnectivity {
                            if(it) setUpLocationListener()
                            else getLastSavedWeatherInfo()
                        }
                        setUpLocationListener()
                    }
                    else -> {
                        showSnackbar(getString(R.string.gps_not_enabled))
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationRequest = LocationRequest().setInterval(LOCATION_REQUEST_TIME_INTERVAL).setFastestInterval(LOCATION_REQUEST_TIME_INTERVAL)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        lat = location.latitude
                        lon = location.longitude
                        if(count < 1) {
                            count++
                            fetchWeatherDataFromCoordinates(lat, lon)
                        }
                    }
                }
            },
            Looper.myLooper()!!
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        setUpForecastRecyclerView()
        observe(viewModel.currentByCityState, ::onCurrentWeatherViewStateChanged)
        observe(viewModel.currentByCoordinatesState, ::onCurrentWeatherViewStateChanged)
        observe(viewModel.forecastByCityState, ::onForecastViewStateChanged)
        observe(viewModel.forecastByCoordinatesState, ::onForecastViewStateChanged)
        observe(viewModel.favoriteLocationState, ::onFavoriteLocationViewStateChanged)
        observe(viewModel.currentByCityData, ::onCurrentWeatherViewDataChanged)
        observe(viewModel.currentByCoordinatesData, ::onCurrentWeatherViewDataChanged)
        observe(viewModel.forecastByCityData, ::onForecastViewDataChanged)
        observe(viewModel.forecastByCoordinatesData, ::onForecastViewDataChanged)
        observe(viewModel.favoriteLocationData, ::onFavoriteLocationViewDataChanged)
        setUpCitySearchView()
        initFavoriteViews()
        viewModel.getFavoriteLocations()
    }

    private fun getLastSavedWeatherInfo(){
        viewModel.getLastSavedCurrentWeather()
        viewModel.getLastSavedForecastWeather()
    }

    private fun setUpCitySearchView(){
        binding.searchEntry.setOnEditorActionListener { _, _, _ ->
            if(!TextUtils.isEmpty(binding.searchEntry.text.toString())){
                fetchWeatherDataByCity(binding.searchEntry.text.toString())
                hideKeyboard()
            }
            false
        }
    }

    private fun initFavoriteViews(){
        binding.favoriteView.setOnClickListener {
            viewModel.getFavoriteLocations()
        }
        binding.addToFavoriteView.setOnClickListener {
            if(favoriteCites.isNotEmpty() && mCurrentWeather != null &&
                favoriteCites.any { it.name == mCurrentWeather?.name + " , " + mCurrentWeather?.sys?.country }){
                deleteCityFromFavorite(mCurrentWeather?.name + " , " + mCurrentWeather?.sys?.country)
                addCityToFavoritesState()
            } else  {
                addCityToFavorite()
            }
        }
    }

    private fun addCityToFavorite(){
        if(currentLocation != null){
            viewModel.saveFavoriteLocation(currentLocation!!)
            addedToFavoritesState()
        }
    }

    private fun deleteCityFromFavorite(cityName: String){
        viewModel.deleteFavoriteLocation(cityName)
    }

    private fun fetchWeatherDataFromCoordinates(lat: Double, lng: Double){
        viewModel.getCurrentWeatherByCoordinates(lat, lng)
        viewModel.getForecastByCoordinates(lat, lng)
    }

    private fun fetchWeatherDataByCity(city: String){
        viewModel.getCurrentWeatherByCity(city)
        viewModel.getForecastByCity(city)
    }

    private fun checkIfLocationSavedAndShowData(){
        binding.addToFavoriteView.show()
        if(mCurrentWeather != null && favoriteCites.any { it.name == mCurrentWeather?.name + " , " + mCurrentWeather?.sys?.country }){
            addedToFavoritesState()
        } else {
            addCityToFavoritesState()
        }
    }

    private fun addedToFavoritesState(){
        binding.addToFavoriteText.text = getString(R.string.added_to_favorites)
        binding.loveImg.setImageResource(R.drawable.ic_loved)
    }
    private fun addCityToFavoritesState(){
        binding.addToFavoriteText.text = getString(R.string.add_city_to_favorites)
        binding.loveImg.setImageResource(R.drawable.ic_love)
    }

    private fun onCurrentWeatherViewDataChanged(currentWeather: CurrentWeather){
        mCurrentWeather = currentWeather
        when{
            currentWeather.weather!![0].main.contains("clouds", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_cloudy)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_cloudy)
            }
            currentWeather.weather!![0].main.contains("clear", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_sunny)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_sunny)
            }
            currentWeather.weather!![0].main.contains("rain", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_rainy)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_rainy)
            }
        }
        checkIfLocationSavedAndShowData()
        binding.todaysDate.text = getDateString(currentWeather.dt ?: 1L)
        binding.location.text = (currentWeather.name + " , " + currentWeather.sys?.country)
        binding.temperature.text = currentWeather.main?.temp.toString().appendTempSign()
        binding.weatherType.text = currentWeather.weather!![0].main
        binding.minTemp.text = currentWeather.main?.tempMin.toString().appendTempSign()
        binding.maxTemp.text = currentWeather.main?.tempMax.toString().appendTempSign()
        binding.currentTemp.text = currentWeather.main?.temp.toString().appendTempSign()
        currentLocation = FavoriteLocation(
            name = currentWeather.name + " , " + currentWeather.sys?.country,
            lat = currentWeather.coord?.lat,
            lng = currentWeather.coord?.lon
        )
    }

    private fun onCurrentWeatherViewStateChanged(currentWeatherViewState: CurrentWeatherViewState){
        when(val dataState = currentWeatherViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> { }
        }
    }

    private fun onForecastViewDataChanged(forecastWeather: ForecastWeather?){
        prepareForecastData(forecastWeather){
            forecastAdapter.submitList(it)
        }
    }

    private fun onForecastViewStateChanged(forecastViewState: ForecastViewState){
        when(val dataState = forecastViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> { }
        }
    }

    private fun onFavoriteLocationViewDataChanged(favoriteLocations: List<FavoriteLocation>){
        if(favoriteLocations.isNotEmpty()) {
            favoriteCites = favoriteLocations
            if(!justLaunched) {
                requireContext().showFavoriteCityDialogs(favoriteCites) { cityName ->
                    fetchWeatherDataByCity(cityName)
                }
            }
        } else {
            showSnackbar(getString(R.string.no_cities_saved_as_favorite))
        }
        justLaunched = false
    }

    private fun onFavoriteLocationViewStateChanged(favoriteLocationViewState: FavoriteLocationViewState){
        when(val dataState = favoriteLocationViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> { }
        }
    }

    private fun setUpForecastRecyclerView(){
        forecastAdapter = ForecastAdapter()
        with(binding.forecastList){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = forecastAdapter
        }
    }

    private fun setUpDependencyInjection(){
        DaggerWeatherComponent
            .builder()
            .coreComponent((appContext as CoreComponentProvider).provideCoreComponent())
            .weatherModule(WeatherModule(this))
            .build()
            .inject(this)
    }

    private fun hideKeyboard() {
        if (activity != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(requireContext()) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            showSnackbar(getString(R.string.gps_not_enabled))
                        }
                    }
                } else {
                    showSnackbar(getString(R.string.location_permission_not_granted))
                }
            }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 419
        const val LOCATION_REQUEST_TIME_INTERVAL = 2000L
    }
}