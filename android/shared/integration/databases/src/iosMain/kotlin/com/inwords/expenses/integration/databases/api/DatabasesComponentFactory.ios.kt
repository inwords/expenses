package com.inwords.expenses.integration.databases.api

import com.inwords.expenses.integration.databases.data.RoomDatabaseBuilderFactory

actual class DatabasesComponentFactory {
    actual interface Deps

    actual fun create(): DatabasesComponent {
        return DatabasesComponent(RoomDatabaseBuilderFactory())
    }

}