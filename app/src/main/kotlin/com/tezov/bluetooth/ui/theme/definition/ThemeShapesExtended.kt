package com.tezov.bluetooth.ui.theme.definition

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Shape

val MaterialTheme.shapesExtended: ThemeShapesExtended.Data
    @Composable
    @ReadOnlyComposable
    get() = ThemeShapesExtended.local.current

object ThemeShapesExtended{

    @Immutable
    data class Data(
        val cardSmall: Shape,
        val cardMedium: Shape,
        val cardLarge: Shape,
        val button: Shape,
        val dialog: Shape,
        val snackbar: Shape,
        val bottomSheet: Shape,
    )
    val local: ProvidableCompositionLocal<Data> = staticCompositionLocalOf {
        error("not provided")
    }

}