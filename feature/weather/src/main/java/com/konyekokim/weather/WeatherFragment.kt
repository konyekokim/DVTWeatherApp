package com.konyekokim.weather

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.konyekokim.commons.extensions.*
import com.konyekokim.commons.ui.getDateString
import com.konyekokim.commons.utils.PermissionUtils
import com.konyekokim.commons.utils.RequestPermissionHandler
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.core.network.responses.WeatherData
import com.konyekokim.core.network.responses.WeatherDataGroup
import com.konyekokim.weather.adapter.ForecastAdapter
import com.konyekokim.weather.databinding.FragmentWeatherBinding
import com.konyekokim.weather.di.DaggerWeatherComponent
import com.konyekokim.weather.di.WeatherModule
import com.konyekokim.weather.utils.showFavoriteCityDialogs
import java.util.*
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val LOCATION_PERMISSION_REQUEST_CODE = 419

    @Inject
    lateinit var viewModel: WeatherViewModel

    private lateinit var binding: FragmentWeatherBinding

    private lateinit var forecastAdapter: ForecastAdapter

    private lateinit var permissionHandler: RequestPermissionHandler

    private var currentLocation: FavoriteLocation? = null
    private var mCurrentWeather: CurrentWeather? = null
    private var favoriteCites: List<FavoriteLocation> = ArrayList()

    var lat = 0.00
    var lon = 0.00
    var count = 0
    var justLaunched = true

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
                        setUpLocationListener()
                    }
                    else -> {
                        //PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    requireActivity() as AppCompatActivity,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
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
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        initPermissions()
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

    private fun setUpCitySearchView(){
        binding.searchEntry.setOnEditorActionListener { _, _, _ ->
            if(!TextUtils.isEmpty(binding.searchEntry.text.toString())){
                fetchWeatherDataByCity(binding.searchEntry.text.toString())
                hideKeyboard()
                true
            }
            false
        }
    }

    private fun initFavoriteViews(){
        binding.favoriteView.setOnClickListener {
            viewModel.getFavoriteLocations()
        }
        binding.addToFavoriteView.setOnClickListener {
            Log.e("Favorite Cities", favoriteCites.toString())
            if(favoriteCites.isNotEmpty() && mCurrentWeather != null && favoriteCites.any { it.name == mCurrentWeather?.name + " , " + mCurrentWeather?.sys?.country }){
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

    private fun initPermissions(){
        initializePermissionHandler()
        handleRequestOfPermissions()
    }

    private fun handleRequestOfPermissions(){
        permissionHandler.requestPermission()
    }

    private fun initializePermissionHandler(){
        permissionHandler = RequestPermissionHandler(
            fragment = this@WeatherFragment,
            permissions = setOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            listener = object: RequestPermissionHandler.Listener{
                override fun onComplete(
                    grantedPermissions: Set<String>,
                    deniedPermissions: Set<String>
                ) {
                    if (Manifest.permission.ACCESS_COARSE_LOCATION in grantedPermissions
                        && Manifest.permission.ACCESS_FINE_LOCATION in grantedPermissions
                    ) {
                        if(PermissionUtils.isLocationEnabled(requireContext()))
                            setUpLocationListener()
                        else
                            showSnackbar("GPS not enabled")
                    }
                }

                override fun onShowPermissionRationale(permissions: Set<String>): Boolean {
                    val message: String =
                        if (Manifest.permission.ACCESS_FINE_LOCATION in permissions
                            || Manifest.permission.ACCESS_FINE_LOCATION in permissions
                        ) {
                            "LOCATION"
                        } else ""
                    AlertDialog.Builder(context).setMessage(
                        "To give you great experience, we need $message permissions"
                    )
                        .setPositiveButton("OK") { _, _ ->
                            permissionHandler.retryRequestDeniedPermission()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            permissionHandler.cancel()
                            dialog.dismiss()
                        }
                        .show()
                    return true
                }

                override fun onShowSettingRationale(permissions: Set<String>): Boolean {
                    AlertDialog.Builder(context)
                        .setMessage("Go Settings -> Permission. Turn on Permissions")
                        .setPositiveButton("Settings") { _, _ ->
                            permissionHandler.requestPermissionInSetting()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            permissionHandler.cancel()
                            dialog.cancel()
                        }
                        .show()
                    return true
                }
            }
        )
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
            else -> {
                //do nothing
            }
        }
    }

    private fun onForecastViewDataChanged(forecastWeather: ForecastWeather){
        prepareForecastData(forecastWeather)
    }

    private fun prepareForecastData(response: ForecastWeather){
        if (response != null) {
            Log.e("Forecast", response.toString())
            val data0: MutableList<WeatherData> = ArrayList<WeatherData>()
            val data1: MutableList<WeatherData> = ArrayList<WeatherData>()
            val data2: MutableList<WeatherData> = ArrayList<WeatherData>()
            val data3: MutableList<WeatherData> = ArrayList<WeatherData>()
            val data4: MutableList<WeatherData> = ArrayList<WeatherData>()
            val data5: MutableList<WeatherData> = ArrayList<WeatherData>()
            val calendar0 = Calendar.getInstance()
            calendar0[Calendar.HOUR_OF_DAY] = 0
            calendar0[Calendar.MINUTE] = 0
            calendar0[Calendar.SECOND] = 0
            calendar0[Calendar.MILLISECOND] = 0
            val calendar1 = calendar0.clone() as Calendar
            calendar1.add(Calendar.DAY_OF_YEAR, 1)
            val calendar2 = calendar0.clone() as Calendar
            calendar2.add(Calendar.DAY_OF_YEAR, 2)
            val calendar3 = calendar0.clone() as Calendar
            calendar3.add(Calendar.DAY_OF_YEAR, 3)
            val calendar4 = calendar0.clone() as Calendar
            calendar4.add(Calendar.DAY_OF_YEAR, 4)
            val calendar5 = calendar0.clone() as Calendar
            calendar5.add(Calendar.DAY_OF_YEAR, 5)
            for (data in response.list!!) {
                when {
                    getCalendarFromDate(data.dt)!!.before(calendar1) -> {
                        data0.add(data)
                    }
                    getCalendarFromDate(data.dt)!!.before(calendar2) -> {
                        data1.add(data)
                    }
                    getCalendarFromDate(data.dt)!!.before(calendar3) -> {
                        data2.add(data)
                    }
                    getCalendarFromDate(data.dt)!!.before(calendar4) -> {
                        data3.add(data)
                    }
                    getCalendarFromDate(data.dt)!!.before(calendar5) -> {
                        data4.add(data)
                    }
                    else -> {
                        data5.add(data)
                    }
                }
            }
            val dataGroup = WeatherDataGroup(data0)
            if (data1.size > 0) dataGroup.addWeatherData(data1)
            if (data2.size > 0) dataGroup.addWeatherData(data2)
            if (data3.size > 0) dataGroup.addWeatherData(data3)
            if (data4.size > 0) dataGroup.addWeatherData(data4)
            if (data5.size > 0) dataGroup.addWeatherData(data5)
            forecastAdapter.submitList(dataGroup.getDataGroup())
        }
    }

    private fun getCalendarFromDate(date: Long): Calendar? {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date * 1000L
        return cal
    }

    private fun onForecastViewStateChanged(forecastViewState: ForecastViewState){
        when(val dataState = forecastViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> {
                //do nothing
            }
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
            else -> {
                //do nothing
            }
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
}