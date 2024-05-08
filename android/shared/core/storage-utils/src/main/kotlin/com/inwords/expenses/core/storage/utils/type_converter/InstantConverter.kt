package com.inwords.expenses.core.storage.utils.type_converter

import androidx.annotation.Keep
import androidx.room.TypeConverter
import kotlinx.datetime.Instant

@Keep
object InstantConverter {
    @TypeConverter
    fun toInstant(dateLong: Long): Instant {
        return Instant.fromEpochMilliseconds(dateLong)
    }

    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }
}