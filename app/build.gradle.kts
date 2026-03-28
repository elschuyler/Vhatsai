plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt") // For Room annotation processing
}

android {
    namespace = "com.elschuyler.vhatsai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.elschuyler.vhatsai"
        minSdk = 24  // Android 7.0 - target for low-end devices
        targetSdk = 34
        versionCode = 1
        versionName = "1.2"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Enable viewBinding in all modules
        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            
            // Disable ProGuard for faster debug builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false  // Keep false for v1; enable later with proper testing
            isShrinkResources = false
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
        freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.webkit.ExperimentalWebKitApi"
        )
    }
    
    // Allow building multiple APKs if needed later (for ABI splits)
    splits {
        abi {
            isEnable = false  // Keep disabled for v1; enable later to reduce APK size
            reset()
            isUniversalApk = true
        }
    }
    
    // Lint configuration - fail build on critical issues only
    lint {
        abortOnError = false  // Don't fail CI on lint warnings for v1
        warningsAsErrors = false
        disable += listOf("UnusedResources", "IconMissingDensityFolder")
    }
}

// Dependency versions - centralized for easier updates
val roomVersion = "2.6.1"
val navVersion = "2.7.6"
val coroutinesVersion = "1.7.3"

dependencies {
    // ========== Core Android ==========
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    
    // ========== WebView (Critical for Vhatsat) ==========
    // Use AndroidX WebView for better compatibility
    implementation("androidx.webkit:webkit:1.9.0")
    
    // ========== Room Database (Local chat history) ==========
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    
    // ========== Navigation ==========
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    
    // ========== Coroutines (Async operations) ==========
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    
    // ========== Lifecycle ==========
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // ========== Preferences (Settings, config) ==========
    implementation("androidx.preference:preference-ktx:1.2.1")
    
    // ========== JSON Parsing (for AI config) ==========
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // ========== File Picker (for config import/export) ==========
    implementation("com.github.DroidNinja:Android-FilePicker:2.2.2") {
        exclude(group = "com.android.support", module = "support-compat")
    }
    
    // ========== Testing ==========
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    
    // Optional: LeakCanary for memory debugging (debug only)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}

// Kapt configuration for Room
kapt {
    correctErrorTypes = true
    useBuildCache = true
}

// Gradle task to print APK info after build (helpful for CI)
tasks.register("printApkInfo") {
    doLast {
        val apkPath = file("build/outputs/apk/debug/app-debug.apk")
        if (apkPath.exists()) {
            println("✅ APK built: ${apkPath.length() / 1024 / 1024} MB")
        } else {
            println("❌ APK not found at expected path")
        }
    }
}

// Ensure printApkInfo runs after assembleDebug
tasks.named("assembleDebug") {
    finalizedBy("printApkInfo")
}
