package com.tezov.shopoclone.ui.navigation.top_app_bar

import androidx.compose.runtime.*
import com.tezov.bluetooth.external_lib_android.ui.State
import com.tezov.shopoclone.ui.MainActivityState

class TopAppBarScreenState(
    private val mainActivityState: MainActivityState
) : State  {

    companion object {
        @Composable
        fun remember(
            mainActivityState: MainActivityState,
        ) = remember {
                TopAppBarScreenState(
                    mainActivityState = mainActivityState,
                )
        }
    }

    val navigationController = mainActivityState.navigationController

}