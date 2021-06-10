package com.konyekokim.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konyekokim.commons.ui.getDay
import com.konyekokim.core.network.responses.WeatherData
import com.konyekokim.weather.R
import com.konyekokim.weather.databinding.ItemForecastBinding

class ForecastAdapter() :
    ListAdapter<WeatherData, ForecastAdapter.WeatherDataItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataItemViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherDataItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDataItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class WeatherDataItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherData) {
            binding.forecastDay.text = getDay(item.dtTxt ?: "")
            binding.forecastTemperature.text = (item.main.temp.toString() + "°C")
            when{
                item.weather[0].main.contains("clouds", true) -> {
                    binding.forecastImage.setImageResource(R.drawable.partly_sunny_icon)
                }
                item.weather[0].main.contains("clear", true) -> {
                    binding.forecastImage.setImageResource(R.drawable.clear_icon)
                }
                item.weather[0].main.contains("rain", true) -> {
                    binding.forecastImage.setImageResource(R.drawable.rain_icon)
                }
                else -> {
                    binding.forecastImage.setImageResource(R.drawable.clear_icon)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WeatherData>() {
            override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
                return oldItem == newItem
            }
        }
    }
}