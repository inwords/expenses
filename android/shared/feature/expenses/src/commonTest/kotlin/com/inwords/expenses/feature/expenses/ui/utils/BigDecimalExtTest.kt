package com.inwords.expenses.feature.expenses.ui.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BigDecimalExtTest {

    @Test
    fun `toRoundedString with default scale returns 2 decimal places for non-whole numbers`() {
        // Arrange
        val value = BigDecimal.parseString("10.123")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("10.12", result)
    }

    @Test
    fun `toRoundedString with default scale returns whole number for exact decimals`() {
        // Arrange
        val value = BigDecimal.parseString("10.00")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("10", result)
    }

    @Test
    fun `toRoundedString with custom scale rounds to specified decimal places`() {
        // Arrange
        val value = BigDecimal.parseString("10.12345")

        // Act
        val result = value.toRoundedString(scale = 3)

        // Assert
        assertEquals("10.123", result)
    }

    @Test
    fun `toRoundedString with custom scale returns whole number when decimals are zero`() {
        // Arrange
        val value = BigDecimal.parseString("10.000")

        // Act
        val result = value.toRoundedString(scale = 3)

        // Assert
        assertEquals("10", result)
    }

    @Test
    fun `toRoundedString handles zero value correctly`() {
        // Arrange
        val value = BigDecimal.ZERO

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("0", result)
    }

    @Test
    fun `toRoundedString handles negative values correctly`() {
        // Arrange
        val value = BigDecimal.parseString("-25.456")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("-25.46", result)
    }

    @Test
    fun `toRoundedString handles negative whole numbers correctly`() {
        // Arrange
        val value = BigDecimal.parseString("-25.00")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("-25", result)
    }

    @Test
    fun `toRoundedString with scale 0 returns integer value`() {
        // Arrange
        val value = BigDecimal.parseString("10.789")

        // Act
        val result = value.toRoundedString(scale = 0)

        // Assert
        assertEquals("11", result)
    }

    @Test
    fun `toRoundedString with scale 1 rounds to one decimal place`() {
        // Arrange
        val value = BigDecimal.parseString("10.56")

        // Act
        val result = value.toRoundedString(scale = 1)

        // Assert
        assertEquals("10.6", result)
    }

    @Test
    fun `toRoundedString with scale 1 returns whole number when decimal is zero`() {
        // Arrange
        val value = BigDecimal.parseString("10.0")

        // Act
        val result = value.toRoundedString(scale = 1)

        // Assert
        assertEquals("10", result)
    }

    @Test
    fun `toRoundedString handles very small positive values`() {
        // Arrange
        val value = BigDecimal.parseString("0.001")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("0", result)
    }

    @Test
    fun `toRoundedString handles very small negative values`() {
        // Arrange
        val value = BigDecimal.parseString("-0.001")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("0", result)
    }

    @Test
    fun `toRoundedString handles large numbers correctly`() {
        // Arrange
        val value = BigDecimal.parseString("1234567.89")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("1234567.89", result)
    }

    @Test
    fun `toRoundedString handles large whole numbers correctly`() {
        // Arrange
        val value = BigDecimal.parseString("1234567.00")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("1234567", result)
    }

    @Test
    fun `toRoundedString rounds up correctly`() {
        // Arrange
        val value = BigDecimal.parseString("10.125")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("10.13", result)
    }

    @Test
    fun `toRoundedString rounds down correctly`() {
        // Arrange
        val value = BigDecimal.parseString("10.124")

        // Act
        val result = value.toRoundedString()

        // Assert
        assertEquals("10.12", result)
    }
}
