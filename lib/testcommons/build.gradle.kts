plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinAndroid)
}

apply(from = "${project.rootDir}/jacoco.gradle")

dependencies {

    implementation(Dependencies.Test.junit)
    implementation(Dependencies.Test.coroutines)
}