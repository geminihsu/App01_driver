# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/geminihsu/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.packetzoom.speed.** { *; }
-keepclassmembers class com.packetzoom.** { *; }
-dontwarn com.packetzoom.**
### For OkHttp 2.x ###
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
### For OkHttp 3.x ###
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable
