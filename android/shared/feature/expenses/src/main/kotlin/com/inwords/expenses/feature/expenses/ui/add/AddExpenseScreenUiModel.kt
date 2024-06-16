package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.runtime.Immutable
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import java.math.BigDecimal

@Immutable
internal data class AddExpenseScreenUiModel(
    val amount: BigDecimal?,
    val currencies: List<CurrencyInfoUiModel>,
    val expenseType: ExpenseType,
    val persons: List<PersonInfoUiModel>,
    val subjectPersons: List<PersonInfoUiModel>,
) {

    @Immutable
    data class CurrencyInfoUiModel(
        val currency: Currency,
        val selected: Boolean,
    )

    @Immutable
    data class PersonInfoUiModel(
        val person: Person,
        val selected: Boolean,
    )

}