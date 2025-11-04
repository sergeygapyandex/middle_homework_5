plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(gradleApi())
}
allprojects {
    repositories {
        mavenCentral()
    }
}