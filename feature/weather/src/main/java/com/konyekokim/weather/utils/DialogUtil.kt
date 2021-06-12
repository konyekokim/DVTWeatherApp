package com.konyekokim.weather.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.weather.R
import com.konyekokim.weather.adapter.FavoriteCityAdapter

fun Context?.showFavoriteCityDialogs(favoriteCities: List<FavoriteLocation>, onCityClicked: (cityName: String) ->  Unit){
    val root = LayoutInflater.from(this)
        .inflate(R.layout.dialog_favorite_cities, null)
    val favoriteCityAdapter = FavoriteCityAdapter {
        onCityClicked(it)
    }
    val dialog = AlertDialog.Builder(this)
        .setView(root).create()
    dialog.setCancelable(true)
    dialog.window?.setBackgroundDrawable(
        ColorDrawable(
            ContextCompat.getColor(this!!, android.R.color.transparent)
        )
    )
    favoriteCityAdapter.submitList(favoriteCities)
    dialog.show()
}