package com.tezov.bluetooth.ui.theme.definition

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle


val MaterialTheme.typographyExtended: ThemeTypographyExtended.Data
    @Composable
    @ReadOnlyComposable
    get() = ThemeTypographyExtended.local.current

object ThemeTypographyExtended{
    @Immutable
    data class Data(
        val topAppBarTitle: TextStyle,
        val bottomNavigationLabel: TextStyle,
        val snackBarMessage: TextStyle,
        val snackBarAction: TextStyle,
    )
    val local: ProvidableCompositionLocal<Data> = staticCompositionLocalOf {
        error("not provided")
    }

}