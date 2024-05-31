package com.inwords.expenses.ui.home

import androidx.lifecycle.ViewModel
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.ui.home.model.HomeScreenUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

internal class HomeViewModel : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    val state = MutableStateFlow(
        HomeScreenUiModel(
            title = "Expenses Home Screen"
        )
    )

}