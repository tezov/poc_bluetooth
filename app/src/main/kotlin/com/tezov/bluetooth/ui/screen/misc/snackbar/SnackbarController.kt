package com.tezov.bluetooth.ui.screen.misc.snackbar

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(
    val scaffoldState: ScaffoldState,
    val coroutineScope: CoroutineScope
) {
    var currentJob:Job? = null

    fun show(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "[X]",
                duration = duration
            )
            when(result){
                SnackbarResult.Dismissed -> {
                    //nothing yet
                }
                SnackbarResult.ActionPerformed -> {
                    //nothing yet
                }
            }
        }
    }
    fun show() {
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = "Not Implemented",
                actionLabel = "[X]",
                duration = SnackbarDuration.Short
            )
            when(result){
                SnackbarResult.Dismissed -> {
                    //nothing yet
                }
                SnackbarResult.ActionPerformed -> {
                    //nothing yet
                }
            }
        }
    }

}