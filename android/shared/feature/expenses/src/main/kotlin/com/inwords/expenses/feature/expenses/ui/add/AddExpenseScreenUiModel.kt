package com.inwords.expenses.feature.expenses.ui.add

import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import kotlinx.collections.immutable.ImmutableList

internal data class AddExpenseScreenUiModel(
    val currencies: ImmutableList<CurrencyInfoUiModel>,
    val expenseType: ExpenseType,
    val persons: ImmutableList<PersonInfoUiModel>,
    val subjectPersons: ImmutableList<PersonInfoUiModel>,
    val equalSplit: Boolean,
    val wholeAmount: String,
    val split: ImmutableList<ExpenseSplitWithPersonUiModel>,
) {

    data class CurrencyInfoUiModel(
        val currencyName: String,
        val currencyCode: String,
        val selected: Boolean,
    )

    data class PersonInfoUiModel(
        val personId: Long,
        val personName: String,
        val selected: Boolean,
    )

    data class ExpenseSplitWithPersonUiModel(
        val person: PersonInfoUiModel,
        val amount: String,
    )

}