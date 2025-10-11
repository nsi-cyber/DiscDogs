import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }


    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {

        androidMain.dependencies {

            implementation(libs.android.mlkitBarcodeScanning)
            implementation(libs.bundles.camera)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)
            implementation("androidx.media3:media3-exoplayer:1.0.1")


        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)

            implementation(libs.bundles.ktor)
            implementation(libs.bundles.coil)
            implementation("com.fleeksoft.ksoup:ksoup:0.1.6")
            implementation("com.fleeksoft.ksoup:ksoup-network:0.1.6")
            //implementation(libs.kotlinx.io.encoding)


        }

        dependencies {
            ksp(libs.androidx.room.compiler)
        }
    }
}

// Load local.properties for API keys
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.discdogs.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.discdogs.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Add API keys to BuildConfig
        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${localProperties.getProperty("GEMINI_API_KEY") ?: "YOUR_GEMINI_API_KEY_HERE"}\""
        )
        buildConfigField(
            "String",
            "DISCOGS_API_KEY",
            "\"${localProperties.getProperty("DISCOGS_API_KEY") ?: "YOUR_DISCOGS_API_KEY_HERE"}\""
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// Android Studio için iOS build task'ları
tasks.register("iosBuild") {
    group = "ios"
    description = "Build iOS framework for Android Studio"
    dependsOn("linkDebugFrameworkIosSimulatorArm64")
}

tasks.register("iosClean") {
    group = "ios"
    description = "Clean iOS build artifacts"
    dependsOn("clean")
    doLast {
        delete("$projectDir/build/xcode-frameworks")
    }
}


