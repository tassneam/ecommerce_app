plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id ("androidx.navigation.safeargs")
}

android {
    buildFeatures{
        viewBinding=true
        dataBinding=true
    }
    namespace = "com.example.ecommerce_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommerce_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    val nav_version = "2.7.5"
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("com.google.android.material:material:1.10.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ROOM
    val roomVersion = "2.6.1"
    implementation ("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation ("androidx.room:room-ktx:$roomVersion")

    // Life Cycle Arch
    val lifecycleVersion = "2.6.2"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    // Annotation processor
    ksp("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
}