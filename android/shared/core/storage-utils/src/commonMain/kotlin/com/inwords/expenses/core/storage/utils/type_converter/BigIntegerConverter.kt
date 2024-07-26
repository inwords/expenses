package com.inwords.expenses.core.storage.utils.type_converter

import androidx.room.TypeConverter
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.util.fromTwosComplementByteArray
import com.ionspin.kotlin.bignum.integer.util.toTwosComplementByteArray

object BigIntegerConverter {
    @TypeConverter
    fun toBigDecimal(bigInteger: ByteArray): BigInteger {
        return BigInteger.fromTwosComplementByteArray(bigInteger)
    }

    @TypeConverter
    fun fromBigDecimal(bigInteger: BigInteger): ByteArray {
        return bigInteger.toTwosComplementByteArray()
    }
}