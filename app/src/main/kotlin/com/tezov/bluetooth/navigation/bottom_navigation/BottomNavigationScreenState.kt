package com.tezov.bluetooth.ui.navigation.bottom_navigation

import androidx.compose.runtime.*
import com.tezov.bluetooth.external_lib_android.ui.State
import com.tezov.shopoclone.ui.MainActivityState

class BottomNavigationScreenState (
    private val mainActivityState: MainActivityState
) : State {

    companion object {
        @Composable
        fun remember(
            mainActivityState: MainActivityState,
        ) = remember {
            BottomNavigationScreenState(
                mainActivityState = mainActivityState,
            )
        }
    }

    val navigationController = mainActivityState.navigationController

}