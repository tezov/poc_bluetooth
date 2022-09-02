package com.tezov.shopoclone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.tezov.bluetooth.external_lib_android.ui.State
import com.tezov.bluetooth.ui.ScreenStateProvider
import com.tezov.bluetooth.ui.navigation.NavigationController
import com.tezov.bluetooth.ui.navigation.bottom_navigation.BottomNavigationScreenState
import com.tezov.bluetooth.ui.screen.misc.bottomsheet.BottomSheetController
import com.tezov.bluetooth.ui.screen.misc.snackbar.SnackbarController
import com.tezov.shopoclone.ui.navigation.top_app_bar.TopAppBarScreenState
import kotlinx.coroutines.CoroutineScope

//todo pass the viewModelProvider to be able
// -use the mainViewModel if useful
// -to pass subViewModel to the corresponding screen

class MainActivityState (
    val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val snackbarController: SnackbarController,
    val bottomSheetController: BottomSheetController,
    val navigationController: NavigationController,
) : State {
    lateinit var topAppBarState: TopAppBarScreenState
    lateinit var bottomNavigationState: BottomNavigationScreenState
    val screenStateProvider = ScreenStateProvider(this)

    companion object {
        @OptIn(ExperimentalAnimationApi::class)
        @Composable
        fun remember(
            coroutineScope: CoroutineScope = rememberCoroutineScope(),
            scaffoldState: ScaffoldState = rememberScaffoldState(),
            snackbarController: SnackbarController = SnackbarController(
                scaffoldState,
                coroutineScope
            ),
            bottomSheetController:BottomSheetController = BottomSheetController(coroutineScope),
            navigationController: NavigationController = NavigationController(
                rememberAnimatedNavController(),
                coroutineScope,
                snackbarController
            ),
        ) = remember {
            MainActivityState(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                snackbarController = snackbarController,
                bottomSheetController = bottomSheetController,
                navigationController = navigationController,
            )
        }
    }


}