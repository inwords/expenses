package com.inwords.expenses.core.ui.utils

import org.jetbrains.compose.resources.StringResource

interface StringProvider {
    suspend fun getString(stringResource: StringResource): String

    suspend fun getString(stringResource: StringResource, vararg formatArgs: Any): String
}

object DefaultStringProvider : StringProvider {
    override suspend fun getString(stringResource: StringResource): String {
        return org.jetbrains.compose.resources.getString(stringResource)
    }

    override suspend fun getString(stringResource: StringResource, vararg formatArgs: Any): String {
        return org.jetbrains.compose.resources.getString(stringResource, *formatArgs)
    }
}
