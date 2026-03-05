# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Gson classes
-keep class com.google.gson.** { *; }
-keep class com.xdustatom.auryxsoda.Level { *; }

# Keep data classes
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
