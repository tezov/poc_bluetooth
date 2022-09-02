package com.tezov.bluetooth.ui.navigation.top_app_bar

import com.tezov.bluetooth.ui.navigation.Route
import com.tezov.bluetooth.R

sealed class TopAppBarItem(var icon:Int, var route: Route){
    companion object{
        val items:List<TopAppBarItem> by lazy{
            listOf(
                Back,
            )
        }
    }

    object Back : TopAppBarItem(R.drawable.ic_arrow_left_24dp, Route.Back)


}