package com.tezov.bluetooth.ui.theme.font

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.tezov.bluetooth.R

val MaterialTheme.fontUbuntu: FontFamily
    @Composable
    @ReadOnlyComposable
    get() = FontUbuntu.local.current

object FontUbuntu{
    val local = compositionLocalOf { fontFamily }
    val fontFamily = FontFamily(
        Font(R.font.ubuntu_light, FontWeight.Light),
        Font(R.font.ubuntu_medium, FontWeight.Medium),
        Font(R.font.ubuntu_bold, FontWeight.Bold),
        Font(R.font.ubuntu_regular, FontWeight.Normal),
    )
}
