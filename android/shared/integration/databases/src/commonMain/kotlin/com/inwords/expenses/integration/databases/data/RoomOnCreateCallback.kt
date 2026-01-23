package com.inwords.expenses.integration.databases.data

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal class RoomOnCreateCallback : RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (1, NULL, 'EUR', 'Euro')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (2, NULL, 'USD', 'US Dollar')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (3, NULL, 'RUB', 'Russian Ruble')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (4, NULL, 'JPY', 'Japanese Yen')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (5, NULL, 'TRY', 'Turkish Lira')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (6, NULL, 'AED', 'UAE Dirham')")
    }
}