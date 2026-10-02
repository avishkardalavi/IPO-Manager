plugins {
    id("com.android.application")
}
android {
    namespace = "com.avishkar.ipomanager"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.avishkar.ipomanager"
        minSdk = 23
        targetSdk = 35
        versionCode = 2
        versionName = "0.2.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation("androidx.room:room-runtime:2.8.5")
    annotationProcessor("androidx.room:room-compiler:2.8.5")
}
