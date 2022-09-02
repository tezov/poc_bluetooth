package com.tezov.bluetooth.ui.navigation.bottom_navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.ui.theme.definition.colorsExtended
import com.tezov.bluetooth.ui.theme.definition.typographyExtended

object BottomNavigationScreen: Screen {

    @Composable
    operator fun invoke(state: BottomNavigationScreenState) {
        content(state)
    }

    @Composable
    private fun content(state: BottomNavigationScreenState) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colorsExtended.bottomNavigationBackground,
        ) {
            BottomNavigationItem.items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.titleResourceId)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = item.titleResourceId),
                            style = MaterialTheme.typographyExtended.bottomNavigationLabel,
                        )
                    },
                    selectedContentColor = MaterialTheme.colorsExtended.bottomNavigationSelected,
                    unselectedContentColor = MaterialTheme.colorsExtended.bottomNavigationUnselected,
                    alwaysShowLabel = true,
                    selected = state.navigationController.currentRoute() == item.route,
                    onClick = {
                        state.navigationController.requestNavigate(item.route, this@BottomNavigationScreen)
                    }
                )
            }
        }
    }
}