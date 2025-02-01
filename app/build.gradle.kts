plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.ognessa.networkdebugger"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ognessa.networkdebugger"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.hiltComposeNavigation)

    implementation(libs.hilt)
    kapt(libs.hiltKapt)
//    implementation(libs.hiltWork)
//    kapt(libs.hiltWorkKapt)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.kc.adapter)

    implementation(libs.serializationJson)
    implementation(libs.retrofit2.ks.converter)

    implementation(libs.loggingInterceptor)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)

    implementation("io.socket:socket.io-client:1.0.2") {
        // excluding org.json which is provided by Android
        exclude(group = "org.json", module = "json")
    }

    implementation(project(":network_debugger"))
}