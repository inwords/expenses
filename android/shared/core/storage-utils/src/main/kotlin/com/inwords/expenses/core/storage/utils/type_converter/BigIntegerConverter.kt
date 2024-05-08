package com.inwords.expenses.core.storage.utils.type_converter

import androidx.annotation.Keep
import androidx.room.TypeConverter
import java.math.BigInteger

@Keep
object BigIntegerConverter {
    @TypeConverter
    fun toBigDecimal(bigInteger: ByteArray): BigInteger {
        return BigInteger(bigInteger)
    }

    @TypeConverter
    fun fromBigDecimal(bigInteger: BigInteger): ByteArray {
        return bigInteger.toByteArray()
    }
}