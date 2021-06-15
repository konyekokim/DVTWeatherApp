plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinAndroid)
    id(Plugins.safeArgs)
}

apply(from = "${project.rootDir}/jacoco.gradle")
apply(from = "${project.rootDir}/detekt.gradle")

dependencies {

    implementation(Dependencies.Kotlin.stdlib)

    // AndroidX
    implementation(Dependencies.AndroidX.Navigation.ui)
    implementation(Dependencies.AndroidX.Fragment.fragmentKtx)
}