package com.tezov.bluetooth.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.tezov.bluetooth.ui.theme.definition.ThemeColorsExtended
import com.tezov.bluetooth.ui.theme.definition.ThemeColorsResource

object ThemeColors {
    object Data {
        val orangeDark: Color = Color(0xFFC3544B)
        val orangeLight: Color = Color(0xFFE06258)
        val orangeGray: Color = Color(0xFFDF9C86)
        val orangeGrayLight: Color = Color(0xFFE4B8AA)
        val whiteGrayDark: Color = Color(0xFF8D8D8D)
        val grayDark: Color = Color(0xFF4F5050)
        val whiteGrayLight: Color = Color(0xFFEFF3F0)
        val blueLight: Color = Color(0xFF6741BB)
        val blackLight: Color = Color(0xFF191B1B)
    }

    val colorsLight = Colors(
        primary = Data.orangeDark,
        primaryVariant = Data.orangeLight,
        onPrimary = Data.blackLight,

        secondary = Data.orangeGray,
        secondaryVariant = Data.orangeGrayLight,
        onSecondary = Data.blackLight,

        surface = Data.whiteGrayLight,
        onSurface = Data.blackLight,

        background = Data.whiteGrayLight,
        onBackground = Data.blackLight,

        error = ThemeColorsResource.Data.red,
        onError = Data.whiteGrayLight,
        isLight = true
    )

    val colorsLightExtended = ThemeColorsExtended.Data(
        onPrimaryLight = ThemeColorsResource.Data.gray,
        onSecondaryLight = ThemeColorsResource.Data.gray,
        onBackgroundLight = Data.whiteGrayDark,

        backgroundElevated = ThemeColorsResource.Data.white,
        onBackgroundElevated = ThemeColorsResource.Data.black,
        onBackgroundElevatedLight = Data.whiteGrayDark,

        backgroundModal = Data.whiteGrayLight,
        onBackgroundModal = ThemeColorsResource.Data.black,
        onBackgroundModalLight = Data.whiteGrayDark,

        backgroundButton = Data.blueLight,
        onBackgroundButton = ThemeColorsResource.Data.white,
        onBackgroundButtonLight = Data.whiteGrayDark,

        topAppBarBackground = colorsLight.primary,
        topAppBarContentText = ThemeColorsResource.Data.white,
        topAppBarContentIcon = ThemeColorsResource.Data.white,

        bottomNavigationBackground = colorsLight.background,
        bottomNavigationSelected = Data.blueLight,
        bottomNavigationUnselected = Data.blackLight,

        snackbarBackground = colorsLight.primaryVariant,
        snackbarMessage = ThemeColorsResource.Data.white,
        snackbarAction = Data.blueLight,
    )
}