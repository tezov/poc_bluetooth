package com.tezov.bluetooth.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.tezov.bluetooth.ui.navigation.bottom_navigation.BottomNavigationScreenState
import com.tezov.bluetooth.ui.screen.misc.bottomsheet.BottomSheetScreenState
import com.tezov.bluetooth.ui.screen.peripheral.InfoScreenState
import com.tezov.bluetooth.ui.screen.peripheral.PeripheralScreenState
import com.tezov.shopoclone.ui.MainActivityState
import com.tezov.shopoclone.ui.navigation.top_app_bar.TopAppBarScreenState
import com.tezov.shopoclone.ui.screen.earning.CentralScreenState

class ScreenStateProvider(private val mainActivityState: MainActivityState) {

    // state provided should be retain to make the user restored screen with the same state he left it
    @Composable
    fun peripheralScreenState() = PeripheralScreenState.remember(mainActivityState = mainActivityState)
    @Composable
    fun centralScreenState() = CentralScreenState.remember(mainActivityState = mainActivityState)
    @Composable
    fun infoScreenState() = InfoScreenState.remember(mainActivityState = mainActivityState)

    @Composable
    fun topAppBarScreenState(): TopAppBarScreenState {
        val topAppBarScreenState = TopAppBarScreenState.remember(mainActivityState = mainActivityState)
        mainActivityState.topAppBarState = topAppBarScreenState
        return topAppBarScreenState
    }
    @Composable
    fun bottomNavigationScreenState(): BottomNavigationScreenState {
        val bottomNavigationScreenState = BottomNavigationScreenState.remember(mainActivityState = mainActivityState)
        mainActivityState.bottomNavigationState = bottomNavigationScreenState
        return bottomNavigationScreenState
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun bottomSheetScreenState(): BottomSheetScreenState {
        val bottomSheetScreenState = BottomSheetScreenState.remember(mainActivityState = mainActivityState)
        mainActivityState.bottomSheetController.bottomSheetState = bottomSheetScreenState
        return bottomSheetScreenState
    }

}