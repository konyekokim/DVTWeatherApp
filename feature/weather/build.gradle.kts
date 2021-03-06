
plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinKapt)
}

apply(from = "${project.rootDir}/jacoco.gradle")
apply(from = "${project.rootDir}/detekt.gradle")

android {
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":lib:commons"))
    implementation(project(":lib:navigation"))
    implementation(project(":lib:resources"))

    implementation(Dependencies.Kotlin.stdlib)

    // AndroidX
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.AndroidX.Fragment.fragment)
    implementation(Dependencies.AndroidX.Fragment.fragmentKtx)
    implementation(Dependencies.AndroidX.Lifecycle.extensions)
    implementation(Dependencies.AndroidX.Lifecycle.viewmodelKtx)
    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.ui)
    implementation(Dependencies.AndroidX.recyclerView)
    implementation(Dependencies.AndroidX.Paging.core)

    implementation(Dependencies.Google.material)

    //Google Play Service
    implementation(Dependencies.GooglePlayService.playServiceLocation)

    // Dagger
    implementation(Dependencies.Dagger.dagger)
    kapt(Dependencies.Dagger.compiler)

    // Retrofit
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.OkHttp.okhttp)

    // Test
    testImplementation(project(":lib:testcommons"))
    testImplementation(Dependencies.Test.archCore)
    testImplementation(Dependencies.Test.coroutines)
    testImplementation(Dependencies.Test.mockk)

    implementation(Dependencies.ConnectionManager.connectionManager)
}