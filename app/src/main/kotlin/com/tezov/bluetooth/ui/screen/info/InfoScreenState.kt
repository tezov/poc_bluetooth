package com.tezov.bluetooth.ui.screen.peripheral

import androidx.compose.runtime.Composable
import com.tezov.bluetooth.external_lib_android.ui.State
import com.tezov.shopoclone.ui.MainActivityState

data class InfoScreenState(
    private val mainActivityState: MainActivityState,
) : State {

    companion object {
        @Composable
        fun remember(
            mainActivityState: MainActivityState,
        ) = androidx.compose.runtime.remember {
            InfoScreenState(
                mainActivityState = mainActivityState,
            )
        }
    }

    //nothing useful yet

}