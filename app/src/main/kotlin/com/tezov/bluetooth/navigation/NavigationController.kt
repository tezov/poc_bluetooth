package com.tezov.bluetooth.ui.navigation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.external_lib_kotlin.util.Event
import com.tezov.bluetooth.ui.navigation.bottom_navigation.BottomNavigationScreen
import com.tezov.bluetooth.ui.navigation.top_app_bar.TopAppBarScreen
import com.tezov.bluetooth.ui.screen.earning.CentralScreen
import com.tezov.bluetooth.ui.screen.misc.snackbar.SnackbarController
import com.tezov.bluetooth.ui.screen.peripheral.InfoScreen
import com.tezov.bluetooth.ui.screen.peripheral.PeripheralScreen
import kotlinx.coroutines.CoroutineScope

class NavigationController(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackbarController: SnackbarController,
) {
    companion object{
        val startRoute = Route.Central
    }

    fun showSnackBarNotImplemented() = snackbarController.show()

    val clickDispatcher = MutableLiveData<Event<Route>>()
    fun dispatchClick(route: Route) {
        clickDispatcher.value = Event(route)
    }
    fun listenClick(lifecycleOwner: LifecycleOwner, observer: Observer<Event<Route>>) {
        clickDispatcher.observe(lifecycleOwner, observer)
    }

    fun currentRoute() = Route.fromString(navController.currentBackStackEntry?.destination?.route)

    private fun navigate(route: Route, builder: NavOptionsBuilder.() -> Unit) =
        navController.navigate(route = route.value(), builder = builder)

    private fun navigate(route: Route) = navController.navigate(route = route.value())
    private fun navigateBack() = navController.popBackStack()

    fun requestNavigate(to: Route, askedBy: Screen) {
        requestNavigate(currentRoute(), to, askedBy)
    }
    fun requestNavigate(from: Route?, to: Route, askedBy: Screen) {
        when (askedBy) {
            TopAppBarScreen -> {
                navigateByTopBar(from, to)
            }
            BottomNavigationScreen -> {
                navigateByBottomBar(from, to)
            }

            PeripheralScreen -> {
                navigateByPeripheralScreen(from, to)
            }
            CentralScreen -> {
                navigateByCentralScreen(from, to)
            }
            InfoScreen -> {
                navigateByInfoScreen(from, to)
            }
            else -> {
                showSnackBarNotImplemented()
            }
        }
    }

    //Navigation bar
    private fun navigateByTopBar(from: Route?, to: Route) {
        when (to) {
            Route.Back -> {
                navigateBack()
            }
            else -> {
                showSnackBarNotImplemented()
            }
        }
    }
    private fun navigateByBottomBar(from: Route?, to: Route) {
         navigate(to) {
             popUpTo(to.value()) {
                 inclusive = false
             }
            launchSingleTop = true
        }
    }

    //Screen
    private fun navigateByPeripheralScreen(from: Route?, to: Route) {
        showSnackBarNotImplemented()
    }
    private fun navigateByCentralScreen(from: Route?, to: Route) {
        showSnackBarNotImplemented()
    }
    private fun navigateByInfoScreen(from: Route?, to: Route) {
        showSnackBarNotImplemented()
    }

}