package com.inwords.expenses.ui.home

import androidx.lifecycle.ViewModel
import com.inwords.expenses.ui.home.model.HomeScreenUiModel
import kotlinx.coroutines.flow.MutableStateFlow

internal class HomeViewModel : ViewModel() {

    val state = MutableStateFlow(
        HomeScreenUiModel(
            title = "Expenses Home Screen"
        )
    )

}