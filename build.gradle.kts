// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("kotlin-kapt") version "1.9.20" apply false
}

// Optional: Define versions in one place for consistency
extra.apply {
    set("roomVersion", "2.6.1")
    set("navVersion", "2.7.6")
    set("coroutinesVersion", "1.7.3")
}
