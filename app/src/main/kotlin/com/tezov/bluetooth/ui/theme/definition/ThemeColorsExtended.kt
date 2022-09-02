package com.tezov.bluetooth.ui.theme.definition

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val MaterialTheme.colorsExtended: ThemeColorsExtended.Data
    @Composable
    @ReadOnlyComposable
    get() = ThemeColorsExtended.local.current

object ThemeColorsExtended {
    @Immutable
    data class Data(
        val onPrimaryLight :Color,
        val onSecondaryLight :Color,
        val onBackgroundLight :Color,

        val backgroundElevated: Color,
        val onBackgroundElevated: Color,
        val onBackgroundElevatedLight:Color,

        val backgroundModal: Color,
        val onBackgroundModal: Color,
        val onBackgroundModalLight:Color,

        val backgroundButton: Color,
        val onBackgroundButton: Color,
        val onBackgroundButtonLight:Color,

        val topAppBarBackground: Color,
        val topAppBarContentText: Color,
        val topAppBarContentIcon: Color,

        val bottomNavigationBackground: Color,
        val bottomNavigationSelected: Color,
        val bottomNavigationUnselected: Color,

        val snackbarBackground: Color,
        val snackbarMessage: Color,
        val snackbarAction: Color,
    )
    val local:ProvidableCompositionLocal<Data> = staticCompositionLocalOf {
        error("not provided")
    }


}




