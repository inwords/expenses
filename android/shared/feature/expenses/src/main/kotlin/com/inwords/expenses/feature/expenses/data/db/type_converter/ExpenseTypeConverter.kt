package com.inwords.expenses.feature.expenses.data.db.type_converter

import androidx.annotation.Keep
import androidx.room.TypeConverter
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType

@Keep
object ExpenseTypeConverter {
    @TypeConverter
    fun toExpenseType(expenseType: String): ExpenseType {
        return when (expenseType) {
            "spending" -> ExpenseType.Spending
            "replenishment" -> ExpenseType.Replenishment
            else -> throw IllegalArgumentException("Unknown expense type: $expenseType")
        }
    }

    @TypeConverter
    fun fromExpenseType(expenseType: ExpenseType): String {
        return when (expenseType) {
            ExpenseType.Spending -> "spending"
            ExpenseType.Replenishment -> "replenishment"
        }
    }
}