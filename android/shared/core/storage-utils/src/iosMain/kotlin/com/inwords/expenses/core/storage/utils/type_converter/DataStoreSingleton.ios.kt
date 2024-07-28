package com.inwords.expenses.core.storage.utils.type_converter

import okio.FileSystem

internal actual val fileSystemSystem: FileSystem
    get() = FileSystem.SYSTEM