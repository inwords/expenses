package com.inwords.expenses.integration.databases.data

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal class RoomOnCreateCallback : RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (1, 0, 'EUR', 'Euro')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (2, 0, 'USD', 'US Dollar')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (3, 0, 'RUB', 'Russian Ruble')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (4, 0, 'JPY', 'Japanese Yen')")
        connection.execSQL("INSERT INTO currency (currency_id, currency_server_id, code, name) VALUES (5, 0, 'TRY', 'Turkish Lira')")
    }
}