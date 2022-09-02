package com.tezov.bluetooth.ui.screen.misc.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tezov.bluetooth.ui.theme.definition.colorsResource
import com.tezov.bluetooth.ui.theme.definition.shapesExtended
import com.tezov.shopoclone.ui.MainActivityState

data class BottomSheetScreenState @OptIn(
    ExperimentalMaterialApi::class
) constructor(
    private val mainActivityState: MainActivityState,
    val bottomSheetState: ModalBottomSheetState,
    private val sheetContentUpdated: MutableState<Int>
) {
    private var _sheetContent: (@Composable () -> Unit) = {
        //hack content bottomsheet can't be null even if not showing
        Box(
            Modifier
                .background(MaterialTheme.colorsResource.transparent)
                .fillMaxWidth()
                .height(1.dp)
        )
    }

    companion object {
        @OptIn(ExperimentalMaterialApi::class)
        @Composable
        fun remember(
            mainActivityState: MainActivityState,
            bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            ),
            sheetContentUpdated: MutableState<Int> = androidx.compose.runtime.remember {
                mutableStateOf(0)
            }
        ) = androidx.compose.runtime.remember {
            BottomSheetScreenState(
                mainActivityState = mainActivityState,
                bottomSheetState = bottomSheetState,
                sheetContentUpdated = sheetContentUpdated,
            )
        }
    }

    @Composable
    fun sheetContent() {
        Card(
            modifier = Modifier.padding(start = 1.dp, end = 1.dp),
            shape = MaterialTheme.shapesExtended.bottomSheet,
            elevation = ModalBottomSheetDefaults.Elevation
        ) {
            if (sheetContentUpdated.value >= 0) {
                _sheetContent()
            }
        }
    }

    fun sheetContent(content: @Composable () -> Unit) {
        _sheetContent = content
        sheetContentUpdated.value++
    }

}