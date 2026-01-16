-processkotlinnullchecks remove_message

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite* {
   <fields>;
}

-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn org.slf4j.**