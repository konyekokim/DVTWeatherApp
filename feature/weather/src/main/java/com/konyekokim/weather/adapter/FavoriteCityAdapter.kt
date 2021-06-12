package com.konyekokim.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.weather.databinding.ItemFavoriteCityBinding

class FavoriteCityAdapter(private val onCityClicked: (cityName: String) ->  Unit)
    :ListAdapter<FavoriteLocation, FavoriteCityAdapter.ViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFavoriteCityBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: FavoriteLocation){
            binding.cityName.text = item.name
            binding.root.setOnClickListener {
                onCityClicked(item.name)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteLocation>() {
            override fun areItemsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
                return oldItem == newItem
            }
        }
    }
}