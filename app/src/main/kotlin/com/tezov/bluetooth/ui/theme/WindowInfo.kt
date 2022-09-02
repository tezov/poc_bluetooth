package com.tezov.bluetooth.clean_architecture.representation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WindowInfo(
    val orientation:Int,
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeigh: Dp,
){
    sealed class WindowType{
        object Compact:WindowType()
        object Medium:WindowType()
        object Extended:WindowType()
    }
    companion object{
        @Composable
        fun remember():WindowInfo{
            val configuration = LocalConfiguration.current
            return WindowInfo(
                configuration.orientation,
                screenWidthInfo = when{
                    configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
                    configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
                    else -> WindowInfo.WindowType.Extended
                },
                screenHeightInfo = when{
                    configuration.screenHeightDp < 480 -> WindowInfo.WindowType.Compact
                    configuration.screenHeightDp < 840 -> WindowInfo.WindowType.Medium
                    else -> WindowInfo.WindowType.Extended
                },
                screenWidth = configuration.screenWidthDp.dp,
                screenHeigh = configuration.screenHeightDp.dp,
            )
        }
    }
}