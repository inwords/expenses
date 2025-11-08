package com.inwords.expenses.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule

class NavModule(
    val serializersModule: SerializersModule,
    val entrySupplier: EntryProviderScope<Destination>.() -> Unit,
)

inline fun <reified T : Destination> NavModule(
    actualSerializer: KSerializer<T>,
    noinline entrySupplier: EntryProviderScope<Destination>.() -> Unit,
): NavModule {
    return NavModule(
        serializersModule = SerializersModule {
            polymorphic(baseClass = NavKey::class, actualClass = T::class, actualSerializer = actualSerializer)
        },
        entrySupplier = entrySupplier,
    )
}