plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}