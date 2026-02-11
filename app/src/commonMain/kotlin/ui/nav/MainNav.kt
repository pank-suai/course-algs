package ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ui.icons.person
import ui.icons.personFilled
import ui.screen.readers.ReadersScreen
import ui.screen.settings.SettingsScreen

sealed interface MainNavItem {
    val icon: ImageVector
    val filledIcon: ImageVector
    val label: String

    object Readers : MainNavItem {
        override val icon: ImageVector = person
        override val filledIcon: ImageVector = personFilled
        override val label: String = "Читатели"
    }

    // TODO: change icon
    object Settings: MainNavItem{
        override val icon = person
        override val filledIcon = personFilled
        override val label = "Настройки"
    }
}

val MAIN_NAV_ITEMS = listOf<MainNavItem>(MainNavItem.Readers, MainNavItem.Settings)

@Composable
fun MainNav() {
    val backStack = remember { mutableStateListOf<MainNavItem>(MainNavItem.Readers) }

    NavigationSuiteScaffold(navigationSuiteItems = {
        MAIN_NAV_ITEMS.forEach {
            val selected = backStack.last() == it
            item(selected, {
                if (it !in backStack) backStack.add(it)
                else {
                    val i = backStack.indexOf(it)
                    backStack.add(backStack[i])
                    backStack.removeAt(i)
                }
            }, {
                Icon(if (selected) it.filledIcon else it.icon, it.label)
            }, label = {
                Text(it.label)
            })
        }
    }){
        NavDisplay(backStack, onBack = {
            if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
        }, entryProvider = entryProvider {
            entry<MainNavItem.Readers>{
                ReadersScreen()
            }
            entry<MainNavItem.Settings> {
                SettingsScreen()
            }
        })
    }
}