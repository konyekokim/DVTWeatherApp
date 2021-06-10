package com.konyekokim.core.di.provider

import com.konyekokim.core.di.CoreComponent

interface CoreComponentProvider {
    fun provideCoreComponent(): CoreComponent
}