package com.tezov.shopoclone.ui.screen.earning

import androidx.compose.runtime.Composable
import com.tezov.bluetooth.external_lib_android.ui.State
import com.tezov.shopoclone.ui.MainActivityState

data class CentralScreenState(
    private val mainActivityState: MainActivityState,
) : State {

    companion object {
        @Composable
        fun remember(
            mainActivityState: MainActivityState,
        ) = androidx.compose.runtime.remember {
            CentralScreenState(
                mainActivityState = mainActivityState,
            )
        }
    }

    //nothing useful yet

}