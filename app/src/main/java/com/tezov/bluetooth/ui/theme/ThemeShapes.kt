package com.tezov.bluetooth.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.tezov.bluetooth.ui.theme.definition.ThemeShapesExtended

object ThemeShapes {

    val shapesExtended = ThemeShapesExtended.Data(
        cardSmall = RoundedCornerShape(4.dp),
        cardMedium = RoundedCornerShape(8.dp),
        cardLarge = RoundedCornerShape(12.dp),

        button = RoundedCornerShape(8.dp),
        dialog = RoundedCornerShape(12.dp),
        snackbar = RoundedCornerShape(12.dp),
        bottomSheet = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    )
}