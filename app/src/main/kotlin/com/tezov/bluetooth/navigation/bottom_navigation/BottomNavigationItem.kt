package com.tezov.bluetooth.ui.navigation.bottom_navigation

import com.tezov.bluetooth.R
import com.tezov.bluetooth.ui.navigation.Route

sealed class BottomNavigationItem(var titleResourceId: Int, var icon: Int, var route: Route) {
    companion object {
        val items: List<BottomNavigationItem> by lazy {
            listOf(
                Peripheral,
                Central,
                Into,
            )
        }
    }

    object Peripheral : BottomNavigationItem(
        R.string.bottom_navigation_peripheral,
        R.drawable.ic_peripheral_24dp,
        Route.Peripheral
    )

    object Central : BottomNavigationItem(
        R.string.bottom_navigation_central,
        R.drawable.ic_central_24dp,
        Route.Central
    )

    object Into : BottomNavigationItem(
        R.string.bottom_navigation_info,
        R.drawable.ic_info_24dp,
        Route.Info
    )
}