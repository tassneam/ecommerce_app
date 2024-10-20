plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
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
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.material.v1100)
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

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.zeugmasolutions.localehelper:locale-helper-android:1.5.1")



//     other dependencies

    //camera
    implementation("androidx.camera:camera-camera2:1.2.0")
    implementation("androidx.camera:camera-lifecycle:1.2.0")
    implementation("androidx.camera:camera-view:1.2.0")
////////////////////////////////////////////////////////////////////////////
    //Glide for more advanced image loading and manipulation
    implementation("com.github.bumptech.glide:glide:4.15.1")
    ksp("com.github.bumptech.glide:ksp:4.15.1")
    //test
    // Glide main library
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Glide transformations library
    implementation("jp.wasabeef:glide-transformations:4.3.0")

    // Kotlin Symbol Processing (KSP) for Glide
    ksp("com.github.bumptech.glide:ksp:4.15.1")

    /* // Firebase BOM
     implementation(platform(libs.firebase.bom))
     //analy
     implementation ("com.google.firebase:firebase-analytics-ktx")
     // Firebase libraries
     implementation(libs.firebase.auth)
     //service auth
     implementation(libs.play.services.auth)
     implementation(libs.google.firebase.firestore)
     ////////////////////////////////
     implementation(libs.google.firebase.storage)
     implementation(libs.google.firebase.database)

     */
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")
    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")
    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")

    //payment implementaion
    // OkHttp for HTTP requests
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

// Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")



}