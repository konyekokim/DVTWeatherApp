package com.konyekokim.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konyekokim.commons.extensions.appendTempSign
import com.konyekokim.commons.ui.getDay
import com.konyekokim.core.network.responses.WeatherData
import com.konyekokim.weather.R
import com.konyekokim.weather.databinding.ItemForecastBinding

class ForecastAdapter :
    ListAdapter<List<WeatherData>, ForecastAdapter.WeatherDataItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataItemViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDataItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDataItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class WeatherDataItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: List<WeatherData>) {
            if(item.isNotEmpty()) {
                binding.forecastDay.text = getDay(item[0].dtTxt ?: "")
                binding.forecastTemperature.text = item[0].main.temp.toString().appendTempSign()
                when {
                    item[0].weather[0].main.contains("clouds", true) -> {
                        binding.forecastImage.setImageResource(R.drawable.partly_sunny_icon)
                    }
                    item[0].weather[0].main.contains("clear", true) -> {
                        binding.forecastImage.setImageResource(R.drawable.clear_icon)
                    }
                    item[0].weather[0].main.contains("rain", true) -> {
                        binding.forecastImage.setImageResource(R.drawable.rain_icon)
                    }
                    else -> {
                        binding.forecastImage.setImageResource(R.drawable.clear_icon)
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<List<WeatherData>>() {
            override fun areItemsTheSame(oldItem: List<WeatherData>, newItem: List<WeatherData>): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: List<WeatherData>, newItem: List<WeatherData>): Boolean {
                return oldItem == newItem
            }
        }
    }
}