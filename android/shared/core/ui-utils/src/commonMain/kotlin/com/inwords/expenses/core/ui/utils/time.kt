package com.inwords.expenses.core.ui.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

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