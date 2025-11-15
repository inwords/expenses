package com.inwords.expenses.integration.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.navigation.BottomSheetSceneStrategy
import com.inwords.expenses.core.navigation.DeeplinkProvider
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.HandleDeeplinks
import com.inwords.expenses.core.navigation.rememberNavigationController
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.events.ui.add_persons.getAddPersonsPaneNavModule
import com.inwords.expenses.feature.events.ui.choose_person.getChoosePersonPaneNavModule
import com.inwords.expenses.feature.events.ui.create.getCreateEventPaneNavModule
import com.inwords.expenses.feature.events.ui.join.getJoinEventPaneNavModule
import com.inwords.expenses.feature.expenses.api.ExpensesComponent
import com.inwords.expenses.feature.expenses.ui.add.getAddExpensePaneNavModule
import com.inwords.expenses.feature.expenses.ui.debts_list.getDebtsListPaneNavModule
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneDestination
import com.inwords.expenses.feature.expenses.ui.list.dialog.getExpenseItemDialogNavModule
import com.inwords.expenses.feature.expenses.ui.list.getExpensesPaneNavModule
import com.inwords.expenses.feature.menu.api.MenuComponent
import com.inwords.expenses.feature.menu.ui.getMenuDialogNavModule
import com.inwords.expenses.feature.settings.api.SettingsComponent
import com.inwords.expenses.feature.share.api.ShareComponent
import kotlinx.serialization.modules.SerializersModule

@Composable
fun MainNavHost(
    deeplinkProvider: DeeplinkProvider,
    modifier: Modifier = Modifier,
    startDestination: Destination = ExpensesPaneDestination
) {
    val navigationController = rememberNavigationController()

    val settingsComponent = remember { ComponentsMap.getComponent<SettingsComponent>() }
    val eventsComponent = remember { ComponentsMap.getComponent<EventsComponent>() }
    val expensesComponent = remember { ComponentsMap.getComponent<ExpensesComponent>() }
    val shareComponent = remember { ComponentsMap.getComponent<ShareComponent>() }
    val menuComponent = remember { ComponentsMap.getComponent<MenuComponent>() } // TODO

    val modules = remember {
        listOf(
            getJoinEventPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
            ),
            getCreateEventPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesScreenDestination = ExpensesPaneDestination,
            ),
            getAddPersonsPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesPaneDestination = ExpensesPaneDestination,
            ),
            getChoosePersonPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                settingsRepository = settingsComponent.settingsRepository,
                expensesScreenDestination = ExpensesPaneDestination,
            ),

            getAddExpensePaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesInteractor = expensesComponent.expensesInteractor,
                settingsRepository = settingsComponent.settingsRepository,
            ),
            getExpensesPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesInteractor = expensesComponent.expensesInteractor,
                settingsRepository = settingsComponent.settingsRepository,
            ),
            getDebtsListPaneNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesInteractor = expensesComponent.expensesInteractor,
            ),
            getExpenseItemDialogNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                expensesInteractor = expensesComponent.expensesInteractor,
                expensesLocalStore = expensesComponent.expensesLocalStore.value,
            ),

            getMenuDialogNavModule(
                navigationController = navigationController,
                eventsInteractor = eventsComponent.eventsInteractor,
                shareManagerLazy = shareComponent.shareManagerLazy,
            )
        )
    }

    @Suppress("UNCHECKED_CAST") // TODO wait for fix in library
    val backStack = rememberNavBackStack(
        configuration = remember {
            SavedStateConfiguration {
                this.serializersModule = SerializersModule {
                    modules.forEach { include(it.serializersModule) }
                }
            }
        },
        elements = arrayOf(startDestination)
    ) as NavBackStack<Destination>
    remember(navigationController, backStack) {
        navigationController.attachTo(backStack)
    }

    HandleDeeplinks(
        deeplinkProvider = deeplinkProvider,
        navigationController = navigationController,
        navDeepLinks = remember { modules.flatMapTo(HashSet()) { it.deepLinks } }
    )

    val strategy = remember {
        BottomSheetSceneStrategy<Destination>() then
            DialogSceneStrategy() then
            SinglePaneSceneStrategy()
    }
    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = backStack::removeLastOrNull,
        sceneStrategy = strategy,
        entryProvider = remember(modules) {
            entryProvider {
                modules.forEach { it.entrySupplier(this) }
            }
        },
    )
}
