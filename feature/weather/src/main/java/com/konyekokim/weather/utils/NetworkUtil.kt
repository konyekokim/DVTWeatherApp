package com.konyekokim.weather.utils

import android.content.Context
import com.androidstudy.networkmanager.Tovuti

fun Context?.checkConnectivity(task: (isConnected: Boolean) -> Unit){
    Tovuti.from(this).monitor { _, isConnected, _ ->
        task(isConnected)
    }
}
