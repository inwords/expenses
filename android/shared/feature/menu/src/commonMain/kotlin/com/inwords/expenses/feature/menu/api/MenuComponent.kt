package com.inwords.expenses.feature.menu.api

import com.inwords.expenses.core.utils.Component
import com.inwords.expenses.feature.events.domain.GetCurrentEventStateUseCase
import com.inwords.expenses.feature.share.api.ShareManager

class MenuComponent(private val deps: Deps) : Component {

    interface Deps {

        val getCurrentEventStateUseCaseLazy: Lazy<GetCurrentEventStateUseCase>
        val shareManager: ShareManager
    }


}