package com.tezov.bluetooth.ui.navigation

import android.util.Log


sealed class Route(private var value:String) {
    open fun value() = value

    companion object{
        val items:List<Route> by lazy {
            listOf(
                Central,
                Peripheral,
                Info,
                Back,
                Peripheral,
            )
        }

        fun fromString(route:String?):Route? {
            return route?.let{ routeToFind-> items.find {
                it.value() == routeToFind
            }}
        }
    }

    object Central : Route("central")
    object Peripheral : Route("peripheral")
    object Info: Route("info")
    object Back: Route("back")
    object NotImplemented: Route("not_implemented")
}