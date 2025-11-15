package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_create
import expenses.shared.feature.expenses.generated.resources.expenses_join
import org.jetbrains.compose.resources.getString

internal class EmptyEventsScreen : BaseScreen() {
    context(extension: ComposeContext)
    suspend fun clickCreateEvent(): CreateEventScreen {
        val createLabel = getString(Res.string.expenses_create)
        extension.onNodeWithText(createLabel).performClick()
        return CreateEventScreen()
    }

    context(extension: ComposeContext)
    suspend fun clickJoinEvent(): JoinEventScreen {
        val joinLabel = getString(Res.string.expenses_join)
        extension.onNodeWithText(joinLabel).performClick()
        return JoinEventScreen()
    }

}
