package com.tezov.bluetooth.ui.navigation.top_app_bar

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.ui.theme.definition.colorsExtended
import com.tezov.bluetooth.ui.theme.definition.typographyExtended
import com.tezov.shopoclone.ui.navigation.top_app_bar.TopAppBarScreenState

object TopAppBarScreen: Screen {

    @Composable
    operator fun invoke(
        state: TopAppBarScreenState,
        titleResourceId: Int,
        leadingItem: TopAppBarItem? = null,
        trailingItem: TopAppBarItem? = null
    ) {
        content(state, titleResourceId, leadingItem, trailingItem)
    }

    @Composable
    private fun content(
        state: TopAppBarScreenState,
        titleResourceId: Int,
        leadingItem: TopAppBarItem? = null,
        trailingItem: TopAppBarItem? = null
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleResourceId),
                    style = MaterialTheme.typographyExtended.topAppBarTitle,
                    color = MaterialTheme.colorsExtended.topAppBarContentText,
                )
            },
            backgroundColor = MaterialTheme.colorsExtended.topAppBarBackground,
            navigationIcon = {
                leadingItem?.let {
                    IconButton(onClick = {
                        state.navigationController.requestNavigate(it.route, this)
                    }) {
                        Icon(
                            painterResource(id = it.icon),
                            null,
                            tint = MaterialTheme.colorsExtended.topAppBarContentIcon
                        )
                    }
                }
            },
            actions = {
                trailingItem?.let {
                    IconButton(onClick = {
                        state.navigationController.requestNavigate(it.route, this@TopAppBarScreen)

                    }) {
                        Icon(
                            painterResource(id = it.icon),
                            null,
                            tint = MaterialTheme.colorsExtended.topAppBarContentIcon
                        )
                    }
                }
            }
        )
    }

}