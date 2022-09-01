package com.tezov.bluetooth.ui.theme.definition

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

val MaterialTheme.dimensionsFontExtended: ThemeDimensionsExtended.DataFont
    @Composable
    @ReadOnlyComposable
    get() = ThemeDimensionsExtended.localFont.current

val MaterialTheme.dimensionsPaddingExtended: ThemeDimensionsExtended.DataPadding
    @Composable
    @ReadOnlyComposable
    get() = ThemeDimensionsExtended.localPadding.current

object ThemeDimensionsExtended{
    @Immutable
    data class DataFont(
        val h1: TextUnit,
        val h2: TextUnit,
        val h3: TextUnit,
        val h4: TextUnit,
        val h5: TextUnit,
        val h6: TextUnit,
        val sub1: TextUnit,
        val sub2: TextUnit,
        val button: TextUnit,
        val bottomNavigationBar: TextUnit,
        val topAppBar: TextUnit,
    )
    val localFont: ProvidableCompositionLocal<DataFont> = staticCompositionLocalOf {
        error("not provided")
    }

    @Immutable
    data class DataPadding(
        //elevation
        val e1:Dp,
        val e2:Dp,
        val e3:Dp,
        //horizontal
        val h1: Dp,
        val h2: Dp,
        val h3: Dp,
        //vertical
        val v1: Dp,
        val v2: Dp,
        val v3: Dp,
        //space vertical
        val sv1: Dp,
        val sv2: Dp,
        val sv3: Dp,
        //space horizontal
        val sh1: Dp,
        val sh2: Dp,
        val sh3: Dp,
    )
    val localPadding: ProvidableCompositionLocal<DataPadding> = staticCompositionLocalOf {
        error("not provided")
    }
}


