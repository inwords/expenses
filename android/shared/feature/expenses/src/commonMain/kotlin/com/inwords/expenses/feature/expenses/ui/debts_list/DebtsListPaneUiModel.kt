package com.inwords.expenses.feature.expenses.ui.debts_list

import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

internal data class DebtsListPaneUiModel(
    val eventName: String,
    val creditors: ImmutableMap<PersonUiModel, ImmutableList<DebtShortUiModel>>,
) {

    data class PersonUiModel(
        val personId: Long,
        val personName: String,
    )
}