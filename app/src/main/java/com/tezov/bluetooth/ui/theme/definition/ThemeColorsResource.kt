package com.tezov.bluetooth.ui.theme.definition

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val MaterialTheme.colorsResource: ThemeColorsResource.Data
    @Composable
    @ReadOnlyComposable
    get() = ThemeColorsResource.local.current

object ThemeColorsResource {

    object Data {
        val transparent: Color = Color.Transparent
        val red: Color = Color.Red
        val blue: Color = Color.Blue
        val green: Color = Color.Green
        val yellow: Color = Color.Yellow
        val black: Color = Color.Black
        val white: Color = Color.White
        val gray: Color = Color.Gray
        val cian: Color = Color.Cyan
        val magenta: Color = Color.Magenta
    }
    val local = staticCompositionLocalOf { Data }

}




