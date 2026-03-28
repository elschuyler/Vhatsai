# ProGuard rules for Vhatsai
# Add custom rules here as needed

# Keep JavascriptInterface methods (required for WebView automation)
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep Room entities and DAOs (for SQLCipher integration in Phase 2)
-keepattributes *Annotation*
-keepclassmembers class **$Companion { *; }

# Keep Kotlin coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
