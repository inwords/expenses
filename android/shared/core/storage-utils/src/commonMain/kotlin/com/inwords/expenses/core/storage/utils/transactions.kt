package com.inwords.expenses.core.storage.utils

import androidx.room.RoomDatabase
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection

class TransactionHelper(dbLazy: Lazy<RoomDatabase>) {

    private val db by dbLazy

    suspend fun <R> immediateWriteTransaction(block: suspend () -> R): R {
        val result = db.useWriterConnection { transactor ->
            transactor.immediateTransaction { block() }
        }
        db.invalidationTracker.refreshAsync() // FIXME: remove when Room is fixed
        return result
    }
}
