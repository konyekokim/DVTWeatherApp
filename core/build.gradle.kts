
plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinKapt)
}

android {
    buildTypes.forEach {
        it.buildConfigField(
            type = "String",
            name = "API_BASE_URL", value = "\"https://api.openweathermap.org/data/2.5/\""
        )
        it.buildConfigField(
            type = "String",
            name = "API_KEY", value = "\"1a91f1370f47f536beb6b3ccc4b110c4\""
        )
        it.buildConfigField(
            type = "String",
            name = "API_UNITS", value = "\"metric\""
        )
        it.buildConfigField(type = "String", name = "WEATHER_DATABASE_NAME", value = "\"weather-db\"")
        it.buildConfigField(type = "int", name = "WEATHER_DATABASE_VERSION", value = "1")
    }
}

dependencies {

    implementation(Dependencies.Kotlin.stdlib)

    implementation(Dependencies.Kotlin.Coroutines.core)

    //AndroidX
    implementation(Dependencies.AndroidX.Room.core)
    kapt(Dependencies.AndroidX.Room.compiler)
    implementation(Dependencies.AndroidX.Room.extensions)

    //Dagger
    implementation(Dependencies.Dagger.dagger)
    kapt(Dependencies.Dagger.compiler)

    //Retrofit-OkHttp
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.OkHttp.okhttp)
    implementation(Dependencies.OkHttp.loggingInterceptor)

}