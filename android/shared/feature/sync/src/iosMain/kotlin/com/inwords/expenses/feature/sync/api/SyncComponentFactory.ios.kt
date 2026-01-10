package com.inwords.expenses.feature.sync.api

import com.inwords.expenses.feature.events.domain.EventsSyncStateHolder
import com.inwords.expenses.feature.events.domain.GetCurrentEventStateUseCase
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.sync.data.EventsSyncManagerFactory

actual class SyncComponentFactory(private val deps: Deps) {

    actual interface Deps {

        actual val getCurrentEventStateUseCaseLazy: Lazy<GetCurrentEventStateUseCase>
        actual val expensesInteractorLazy: Lazy<ExpensesInteractor>
        actual val eventsSyncStateHolderLazy: Lazy<EventsSyncStateHolder>
    }

    actual fun create(): SyncComponent {
        val syncManagerFactory = EventsSyncManagerFactory()
        return SyncComponent(eventsSyncManagerFactory = syncManagerFactory, deps = deps)
    }

}
