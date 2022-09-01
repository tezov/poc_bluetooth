package com.tezov.bluetooth.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.tezov.bluetooth.ui.theme.ThemeDimensions.dimensionsFontExtended
import com.tezov.bluetooth.ui.theme.definition.ThemeTypographyExtended
import com.tezov.bluetooth.ui.theme.font.FontRoboto
import com.tezov.bluetooth.ui.theme.font.FontUbuntu

object ThemeTypography {
    val typography = androidx.compose.material.Typography(
        h1 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h1
        ),
        h2 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h2
        ),
        h3 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h3
        ),
        h4 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h4
        ),
        h5 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h5
        ),
        h6 = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.h6
        ),
        subtitle1 = TextStyle(
            fontFamily = FontUbuntu.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.sub1
        ),
        subtitle2 = TextStyle(
            fontFamily = FontUbuntu.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionsFontExtended.sub2
        ),
        button  = TextStyle(
            fontFamily = FontRoboto.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionsFontExtended.button,
        ),
        //todo other style
    )

    val typographyExtended = ThemeTypographyExtended.Data(
        topAppBarTitle = TextStyle(
            fontFamily = FontUbuntu.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.topAppBar
        ),
        bottomNavigationLabel = TextStyle(
            fontFamily = FontUbuntu.fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = dimensionsFontExtended.bottomNavigationBar
        ),
        snackBarMessage = typography.h3,
        snackBarAction = typography.h4,
    )
}