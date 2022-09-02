package com.tezov.bluetooth.ui.screen.misc.snackbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.ui.theme.definition.*
import com.tezov.bluetooth.ui.theme.misc.padding

object SnackbarInfoScreen : Screen {

    @Composable
    operator fun invoke(snackbarHostState: SnackbarHostState) {
        content(snackbarHostState)
    }

    @Composable
    private fun content(snackbarHostState: SnackbarHostState) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimensionsPaddingExtended.h3,
                    bottom = MaterialTheme.dimensionsPaddingExtended.sv2
                )
        ) { data ->
            Snackbar(
                backgroundColor = MaterialTheme.colorsExtended.snackbarBackground,
                elevation = 0.dp,
                shape = MaterialTheme.shapesExtended.snackbar,
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typographyExtended.snackBarMessage,
                        color = MaterialTheme.colorsExtended.snackbarMessage,
                    )
                },
                action = {
                    data.actionLabel?.let { label ->
                        TextButton(
                            onClick = { data.performAction() }) {
                            Text(
                                text = label,
                                style = MaterialTheme.typographyExtended.snackBarAction,
                                color = MaterialTheme.colorsExtended.snackbarAction,
                            )
                        }
                    }
                }
            )
        }
    }


}