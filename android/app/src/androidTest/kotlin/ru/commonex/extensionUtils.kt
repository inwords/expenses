package ru.commonex

import androidx.activity.ComponentActivity
import de.mannodermaus.junit5.compose.AndroidComposeExtension
import de.mannodermaus.junit5.compose.ComposeContext
import kotlinx.coroutines.runBlocking


internal inline fun <reified A : ComponentActivity> AndroidComposeExtension<A>.runTest(crossinline block: suspend ComposeContext.() -> Unit) {
    use {
        runBlocking {
            block()
        }
    }
}
