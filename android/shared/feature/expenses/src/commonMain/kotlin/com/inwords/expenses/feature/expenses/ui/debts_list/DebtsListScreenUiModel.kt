package com.inwords.expenses.feature.expenses.ui.debts_list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

internal data class DebtsListScreenUiModel(
    val eventName: String,
    val creditors: ImmutableMap<PersonUiModel, ImmutableList<DebtorShortUiModel>>,
) {

    data class PersonUiModel(
        val personId: Long,
        val personName: String,
    )

    data class DebtorShortUiModel(
        val person: PersonUiModel,
        val currencyCode: String,
        val currencyName: String,
        val amount: String,
    )

}