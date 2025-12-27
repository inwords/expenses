package ru.commonex

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class Offline

internal class ConnectivityExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        val testMethod = context.requiredTestMethod
        if (testMethod.getAnnotation(Offline::class.java) != null) {
            ConnectivityManager.turnOffDataAndWifi()
        }
    }

    override fun afterEach(context: ExtensionContext) {
        val testMethod = context.requiredTestMethod
        if (testMethod.getAnnotation(Offline::class.java) != null) {
            ConnectivityManager.turnOnData()
        }
    }
}
