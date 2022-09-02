package com.tezov.bluetooth.ui.screen.misc.bottomsheet

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.ui.theme.definition.colorsResource

object BottomSheetScreen: Screen {

    @Composable
    operator fun invoke(state:BottomSheetScreenState, content:@Composable () -> Unit) {
        content(state,  content)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun content(state:BottomSheetScreenState, content:@Composable () -> Unit) {
        ModalBottomSheetLayout(
            sheetContentColor = MaterialTheme.colorsResource.transparent,
            sheetBackgroundColor = MaterialTheme.colorsResource.transparent,
            sheetState = state.bottomSheetState,
            sheetShape = RectangleShape,
            sheetElevation = 0.dp,
            sheetContent = { state.sheetContent() },
            content = content
        )
    }



}