package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency

class EventCreationStateHolder internal constructor() {
    private var draftEventName: String = ""
    private var draftPrimaryCurrencyId: Long = 0
    private var draftOwner: String = ""
    private var draftOtherPersons: List<String> = emptyList()

    internal fun draftEventName(eventName: String) {
        draftEventName = eventName.trim()
    }

    internal fun draftEventPrimaryCurrency(currency: Currency) {
        draftPrimaryCurrencyId = currency.id
    }

    internal fun draftOwner(owner: String) {
        draftOwner = owner.trim()
    }

    internal fun draftOtherPersons(persons: List<String>) {
        draftOtherPersons = persons.map { it.trim() }.filter { it.isNotEmpty() }
    }

    internal fun clear() {
        draftEventName = ""
        draftPrimaryCurrencyId = 0
        draftOwner = ""
        draftOtherPersons = emptyList()
    }

    internal fun getDraftEventName(): String = draftEventName
    internal fun getDraftPrimaryCurrencyId(): Long = draftPrimaryCurrencyId
    internal fun getDraftOwner(): String = draftOwner
    internal fun getDraftOtherPersons(): List<String> = draftOtherPersons
}
