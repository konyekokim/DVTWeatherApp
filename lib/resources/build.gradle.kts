plugins {
    id(Plugins.androidLibrary)
}

apply(from = "${project.rootDir}/jacoco.gradle")
apply(from = "${project.rootDir}/detekt.gradle")

dependencies {
    implementation(Dependencies.Google.material)
}