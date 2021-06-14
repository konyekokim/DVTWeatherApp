plugins {
    id(Plugins.androidLibrary)
}

apply(from = "${project.rootDir}/jacoco.gradle")

dependencies {
    implementation(Dependencies.Google.material)
}