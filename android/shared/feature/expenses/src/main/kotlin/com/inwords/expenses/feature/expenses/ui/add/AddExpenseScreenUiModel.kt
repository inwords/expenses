package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.runtime.Stable
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import java.math.BigDecimal

@Stable
internal data class AddExpenseScreenUiModel(
    val amount: BigDecimal?,
    val currencies: List<CurrencyInfoUiModel>,
    val expenseType: ExpenseType,
    val persons: List<PersonInfoUiModel>,
    val subjectPersons: List<PersonInfoUiModel>,
) {

    data class CurrencyInfoUiModel(
        val currency: Currency,
        val selected: Boolean,
    )

    data class PersonInfoUiModel(
        val person: Person,
        val selected: Boolean,
    )

}