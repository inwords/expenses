package com.inwords.expenses.core.ui.utils

import kotlinx.datetime.format.DateTimeFormat

internal class LazyFormatMap<T : Any>(
    keys: Set<String>,
    keyFallbackMap: Map<String, String>,
    createValueForKey: (String) -> DateTimeFormat<T>,
    lazyThreadSafetyMode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) {

    private val map = buildMap(capacity = keys.size + keyFallbackMap.size) {
        keys.forEach { key ->
            put(key, lazy(mode = lazyThreadSafetyMode) { createValueForKey(key) })
        }
        keyFallbackMap.forEach { (key, fallbackKey) ->
            if (key in this) {
                throw IllegalArgumentException("Key '$key' is already present in the map, cannot set fallback to '$fallbackKey'")
            } else {
                val fallbackLazy = this[fallbackKey]
                if (fallbackLazy != null) {
                    put(key, fallbackLazy)
                } else {
                    throw IllegalArgumentException("Fallback key '$fallbackKey' for key '$key' is not present in the map")
                }
            }
        }
    }

    fun getValue(key: String): DateTimeFormat<T> {
        return map[key]?.value ?: throw NoSuchElementException("Key '$key' is not present in the map")
    }

}