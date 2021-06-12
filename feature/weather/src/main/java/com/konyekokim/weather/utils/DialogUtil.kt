package com.konyekokim.weather.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.weather.adapter.FavoriteCityAdapter
import com.konyekokim.weather.databinding.DialogFavoriteCitiesBinding

fun Context?.showFavoriteCityDialogs(favoriteCities: List<FavoriteLocation>, onCityClicked: (cityName: String) ->  Unit){
    val bind: DialogFavoriteCitiesBinding = DialogFavoriteCitiesBinding.inflate(LayoutInflater.from(this))
    val dialog = AlertDialog.Builder(this)
        .setView(bind.root).create()
    dialog.setCancelable(true)
    dialog.window?.setBackgroundDrawable(
        ColorDrawable(
            ContextCompat.getColor(this!!, android.R.color.transparent)
        )
    )
    val favoriteCityAdapter = FavoriteCityAdapter {
        onCityClicked(it)
        dialog.dismiss()
    }
    with(bind.favoriteCityList){
        layoutManager = LinearLayoutManager(this@showFavoriteCityDialogs, LinearLayoutManager.VERTICAL, false)
        adapter = favoriteCityAdapter
    }
    favoriteCityAdapter.submitList(favoriteCities)
    dialog.show()
}