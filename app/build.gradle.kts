plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.tech.instasaver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tech.instasaver"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.media3:media3-ui:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.3")

    implementation("androidx.compose.material:material-icons-extended")  //extend icons file in compose

    //for image picker coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-alpha02")

    //livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation("com.google.accompanist:accompanist-pager:0.20.2")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.20.2")
    implementation("androidx.compose.foundation:foundation:1.5.1")

    implementation("androidx.compose.ui:ui-util:1.5.1")

    //dagger hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")  //hilt viewModel

    //coroutine
    //noinspection GradleDependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")
}
kapt {
    correctErrorTypes
    true
}