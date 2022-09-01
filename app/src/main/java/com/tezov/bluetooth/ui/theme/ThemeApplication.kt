package com.tezov.bluetooth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.tezov.bluetooth.ui.theme.ThemeColors.colorsLight
import com.tezov.bluetooth.ui.theme.ThemeColors.colorsLightExtended
import com.tezov.bluetooth.ui.theme.ThemeDimensions.dimensionsFontExtended
import com.tezov.bluetooth.ui.theme.ThemeDimensions.dimensionsPaddingExtended
import com.tezov.bluetooth.ui.theme.ThemeShapes.shapesExtended
import com.tezov.bluetooth.ui.theme.ThemeTypography.typography
import com.tezov.bluetooth.ui.theme.ThemeTypography.typographyExtended
import com.tezov.bluetooth.ui.theme.definition.*
import com.tezov.bluetooth.ui.theme.font.FontRoboto
import com.tezov.bluetooth.ui.theme.font.FontUbuntu

object ThemeApplication {
    @Composable
    fun Bluetooth(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
        CompositionLocalProvider(
            //resource
            ThemeColorsResource.local provides ThemeColorsResource.Data,
            //font
            FontRoboto.local provides FontRoboto.fontFamily,
            FontUbuntu.local provides FontUbuntu.fontFamily,
            //theme extension
            ThemeColorsExtended.local provides colorsLightExtended,
            ThemeShapesExtended.local provides shapesExtended,
            ThemeDimensionsExtended.localFont provides dimensionsFontExtended,
            ThemeDimensionsExtended.localPadding provides dimensionsPaddingExtended,
            ThemeTypographyExtended.local provides typographyExtended,
        ) {
            MaterialTheme(
                colors = colorsLight,
                typography = typography,
                shapes = ThemeShapesResource.Default,
                content = content
            )
        }
    }
}








