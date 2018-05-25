-printconfiguration "build/outputs/mapping/configuration.txt"

-dontwarn java.lang.invoke.*

-keepattributes *Annotation*
-keep class javax.inject.** { *; }

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# GSON
-keepattributes Signature

# Missing annotations are harmless.
-dontwarn sun.misc**
-dontwarn javax.annotation.**

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Remove Log
-assumenosideeffects class android.util.Log {
  public static boolean isLoggable(java.lang.String, int);
  public static *** v(...);
  public static *** i(...);
  public static *** w(...);
  public static *** d(...);
  public static *** e(...);
}

-dontwarn kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck

-optimizationpasses 5

# Keep the names of the model and event classes
-keepnames public class * extends **Event
-keepnames public class * extends **Model

# Keep custom exceptions
-keep public class * extends java.lang.Exception
