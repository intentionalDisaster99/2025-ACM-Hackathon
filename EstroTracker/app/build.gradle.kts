import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

configure<ApplicationExtension> {
    namespace = "com.hacksolotls.estrotracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hacksolotls.estrotracker"
        minSdk = 26
        targetSdk = 35 // Aligned with compileSdk for best behavior
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

// Modern Kotlin Compiler configuration
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        // Helps with the Hilt/KSP metadata issue
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

dependencies {

    implementation(libs.androidx.compose.runtime.livedata)
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
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Estresso
    implementation(libs.estresso)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-compiler:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Charts
    //implementation ("io.github.ehsannarmani:compose-charts:0.2.5")

    // Room
    val room_version = "2.8.4"

    // Essential: The library that runs in your app
    implementation("androidx.room:room-runtime:$room_version")

    // Essential: The compiler that generates code (choose KSP for Kotlin)
    ksp("androidx.room:room-compiler:$room_version")

    // Optional: Kotlin Extensions and Coroutines support (Highly Recommended)
    implementation("androidx.room:room-ktx:$room_version")

    // Optional: Testing helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // Optional: Paging 3 integration
    implementation("androidx.room:room-paging:$room_version")

    // Lifecycle
    val lifecycle_version = "2.8.0" // Check for the latest stable version

    // ViewModel - essential for UI data management
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // Lifecycle Runtime - for lifecycleScope and basic lifecycle support
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    // LiveData - (Optional) if you aren't using Kotlin Flows
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Jetpack Compose Integration (if using Compose)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    // Notifications
    val work_version = "2.11.2"

    // Standard Kotlin + Coroutines support (Recommended)
    implementation("androidx.work:work-runtime-ktx:$work_version")

    implementation(libs.compose.material.icons)


    // Vico
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m2)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.views)
}