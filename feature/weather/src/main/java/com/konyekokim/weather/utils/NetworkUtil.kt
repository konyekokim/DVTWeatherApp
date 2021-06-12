package com.konyekokim.weather.utils

import android.content.Context
import com.androidstudy.networkmanager.Tovuti

fun checkConnectivity(context: Context,
                      task: (isConnected: Boolean) -> Unit){
    Tovuti.from(context).monitor { _, isConnected, _ ->
        task(isConnected)
    }
}
