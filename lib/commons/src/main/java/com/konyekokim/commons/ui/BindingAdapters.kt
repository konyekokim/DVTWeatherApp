package com.konyekokim.commons.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.konyekokim.commons.extensions.loadImage

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setUrl(imageView: ImageView, imageUrl: String?){
        imageView.loadImage(imageUrl)
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if(visible) View.VISIBLE else View.GONE
    }
}