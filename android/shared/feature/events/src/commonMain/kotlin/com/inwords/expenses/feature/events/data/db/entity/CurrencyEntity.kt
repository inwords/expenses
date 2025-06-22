package com.inwords.expenses.feature.events.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CurrencyEntity.TABLE_NAME)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(ColumnNames.ID)
    val currencyId: Long = 0L,

    @ColumnInfo(ColumnNames.SERVER_ID)
    val currencyServerId: String?,

    @ColumnInfo(ColumnNames.CODE)
    val code: String,

    @ColumnInfo(ColumnNames.NAME)
    val name: String,
) {
    companion object {

        const val TABLE_NAME = "currency"
    }

    object ColumnNames {

        const val ID = "currency_id"
        const val SERVER_ID = "currency_server_id"
        const val CODE = "code"
        const val NAME = "name"
    }
}