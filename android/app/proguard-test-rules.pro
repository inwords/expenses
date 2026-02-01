
-dontwarn javax.lang.model.element.Modifier

-dontwarn java.lang.**
-dontwarn jdk.jfr.**
-dontwarn kotlin.reflect.**

# Marathon adam test annotation producer for remote test parsing
-keep class com.malinskiy.adam.junit4.android.listener.** { *; }