package com.inwords.expenses.feature.expenses.ui.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.ExpenseSplitWithPersonUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun ExpensesScreenSuccessPreview() {
    AddExpenseScreen(
        onCurrencyClicked = {},
        onExpenseTypeClicked = {},
        onPersonClicked = {},
        onSubjectPersonClicked = {},
        onEqualSplitChange = {},
        onWholeAmountChanged = {},
        onSplitAmountChanged = { _, _ -> },
        onConfirmClicked = {},
        onCloseClicked = {},
        state = SimpleScreenState.Success(mockAddExpenseScreenUiModel()),
    )
}

internal fun mockAddExpenseScreenUiModel(): AddExpenseScreenUiModel {
    val person1 = Person(
        id = 1,
        name = "Василий"
    )
    val person2 = Person(
        id = 2,
        name = "Максим"
    )
    val person3 = Person(
        id = 3,
        name = "Анжела"
    )
    val person4 = Person(
        id = 4,
        name = "Саша"
    )
    return AddExpenseScreenUiModel(
        currencies = persistentListOf(
            CurrencyInfoUiModel(
                currencyCode = "USD",
                currencyName = "US Dollar",
                selected = true
            ),
            CurrencyInfoUiModel(
                currencyCode = "EUR",
                currencyName = "Euro",
                selected = false
            ),
            CurrencyInfoUiModel(
                currencyCode = "RUB",
                currencyName = "Russian Ruble",
                selected = false
            ),
        ),
        expenseType = ExpenseType.Spending,
        persons = persistentListOf(
            PersonInfoUiModel(
                personId = person1.id,
                personName = person1.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person2.id,
                personName = person2.name,
                selected = false
            ),
            PersonInfoUiModel(
                personId = person3.id,
                personName = person3.name,
                selected = false
            ),
            PersonInfoUiModel(
                personId = person4.id,
                personName = person4.name,
                selected = false
            ),
        ),
        subjectPersons = persistentListOf(
            PersonInfoUiModel(
                personId = person1.id,
                personName = person1.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person2.id,
                personName = person2.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person3.id,
                personName = person3.name,
                selected = true
            ),
            PersonInfoUiModel(
                personId = person4.id,
                personName = person4.name,
                selected = true
            ),
        ),
        equalSplit = false,
        wholeAmount = "",
        split = persistentListOf(
            ExpenseSplitWithPersonUiModel(
                person = PersonInfoUiModel(
                    personId = person1.id,
                    personName = person1.name,
                    selected = true
                ),
                amount = "33"
            ),
            ExpenseSplitWithPersonUiModel(
                person = PersonInfoUiModel(
                    personId = person2.id,
                    personName = person2.name,
                    selected = true
                ),
                amount = "34"
            ),
        )
    )
}
