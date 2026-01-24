package com.inwords.expenses.core.ui.utils

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

private val fallbackToRussianCodes = setOf(
    "ru", "by",
)

private val MonthNames.Companion.RUSSIAN_FULL by lazy(mode = LazyThreadSafetyMode.PUBLICATION) {
    MonthNames(
        january = "января",
        february = "февраля",
        march = "марта",
        april = "апреля",
        may = "мая",
        june = "июня",
        july = "июля",
        august = "августа",
        september = "сентября",
        october = "октября",
        november = "ноября",
        december = "декабря",
    )
}

val fullDateFormat = LocalDate.Format {
    day(padding = Padding.ZERO)
    char(' ')
    monthName(
        if (Locale.current.language in fallbackToRussianCodes) {
            MonthNames.RUSSIAN_FULL
        } else {
            MonthNames.ENGLISH_FULL
        }
    )
    char(' ')
    year(padding = Padding.ZERO)
}

val defaultDateFormat = LocalDateTime.Format {
    date(
        LocalDate.Format {
            day(padding = Padding.ZERO)
            char('.')
            monthNumber()
            char('.')
            yearTwoDigits(2000)
        }
    )
    char(' ')
    time(
        LocalTime.Format {
            hour(); char(':'); minute()
        }
    )
}
