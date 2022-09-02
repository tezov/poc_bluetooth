package com.tezov.bluetooth.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.tezov.bluetooth.ui.MainActivity
import com.tezov.bluetooth.R
import com.tezov.bluetooth.ui.screen.earning.CentralScreen
import com.tezov.bluetooth.ui.screen.peripheral.InfoScreen
import com.tezov.bluetooth.ui.screen.peripheral.PeripheralScreen
import com.tezov.shopoclone.ui.MainActivityState

object NavigationGraph {
    private const val TRANSITION_DURATION_ms = 350

    @Composable
    operator fun invoke(mainActivityState: MainActivityState) {
        graph(mainActivityState)
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun graph(mainActivityState: MainActivityState) {
        AnimatedNavHost(
            navController = mainActivityState.navigationController.navController,
            startDestination = NavigationController.startRoute.value(),
        ) {

            composableWithAnimation(Route.Peripheral) {
                MainActivity.Screen.withTopAppBarAndBottomNavigationBar(
                    mainActivityState,
                    R.string.top_app_bar_peripheral,
                    null,
                    null
                ) {
                    PeripheralScreen(mainActivityState.screenStateProvider.peripheralScreenState(), innerPadding = it)
                }
            }

            composableWithAnimation(Route.Central) {
                MainActivity.Screen.withTopAppBarAndBottomNavigationBar(
                    mainActivityState,
                    R.string.top_app_bar_central,
                    null,
                    null
                ) {
                    CentralScreen(mainActivityState.screenStateProvider.centralScreenState(), innerPadding = it)
                }
            }

            composableWithAnimation(Route.Info) {
                MainActivity.Screen.withTopAppBarAndBottomNavigationBar(
                    mainActivityState,
                    R.string.top_app_bar_info,
                    null,
                    null
                ) {
                    InfoScreen(mainActivityState.screenStateProvider.infoScreenState(), innerPadding = it)
                }
            }


        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    private fun NavGraphBuilder.composableWithAnimation(
        route: Route,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
    ) = composable(
        route = route.value(),
        arguments = arguments,

        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = {  EnterTransition.None },
        popExitTransition = { ExitTransition.None },

//        enterTransition = {
//            fadeIn(animationSpec = tween(TRANSITION_DURATION_ms))
//        },
//        exitTransition = {
//            fadeOut(animationSpec = tween(TRANSITION_DURATION_ms))
//        },
//        popEnterTransition = {
//            fadeIn(animationSpec = tween(TRANSITION_DURATION_ms))
//        },
//        popExitTransition = {
//            fadeOut(animationSpec = tween(TRANSITION_DURATION_ms))
//        },

        content = content
    )


}