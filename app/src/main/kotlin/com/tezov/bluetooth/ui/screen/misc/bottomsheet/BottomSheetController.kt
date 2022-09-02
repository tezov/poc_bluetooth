package com.tezov.bluetooth.ui.screen.misc.bottomsheet

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BottomSheetController(val coroutineScope: CoroutineScope) {
    lateinit var bottomSheetState: BottomSheetScreenState

    var currentJob:Job? = null

    @OptIn(ExperimentalMaterialApi::class)
    fun show(content: @Composable ()->Unit) {
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            bottomSheetState.sheetContent(content)
            bottomSheetState.bottomSheetState.show()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun hide() {
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            bottomSheetState.bottomSheetState.hide()
        }
    }

}