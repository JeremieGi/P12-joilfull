plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.openclassrooms.joilfull"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.openclassrooms.joilfull"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        // Configure additional Java compiler arguments
        tasks.withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:deprecation")
        }

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
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


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //Tests de routines avec des Mocks
    testImplementation(libs.mockk)
    testImplementation (libs.kotlinx.coroutines.test)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Gestion des tailles d'écran
    //implementation("androidx.compose.material3:material3-window-size-class:1.2.1")


    // ViewModel avec Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.moshi)
    // Moshi JSON Library
    implementation(libs.moshi.kotlin)


    // Glide
    implementation(libs.compose)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.hilt.navigation.fragment)
    kapt(libs.hilt.compiler)

    // Navigation avec Compose
    implementation(libs.androidx.navigation.compose)

    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.ui.text.google.fonts) // Font Open Sans


    implementation(libs.androidx.hilt.navigation.compose)
}