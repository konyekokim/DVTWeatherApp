package com.konyekokim.core.di.modules

import com.konyekokim.core.data.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class DispatcherModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() =
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
}