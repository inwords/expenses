package com.inwords.expenses.integration.databases.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/**
 * Migration from version 1 to 2: Adds AED (UAE Dirham) currency support.
 * 
 * This migration inserts AED into the currency table if it doesn't already exist,
 * making it idempotent and safe to run multiple times.
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            INSERT INTO currency (currency_server_id, code, name) 
            SELECT NULL, 'AED', 'UAE Dirham'
            WHERE NOT EXISTS (SELECT 1 FROM currency WHERE code = 'AED')
            """.trimIndent()
        )
    }
}
