package com.tezov.bluetooth.ui.screen.earning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.tezov.bluetooth.external_lib_android.ui.Screen
import com.tezov.bluetooth.ui.theme.definition.colorsResource
import com.tezov.bluetooth.ui.theme.definition.dimensionsPaddingExtended
import com.tezov.shopoclone.ui.screen.earning.CentralScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CentralScreen : Screen{

    @Composable
    operator fun invoke(state: CentralScreenState, innerPadding: PaddingValues) {
        content(state, innerPadding)
    }

    @Composable
    fun content(state: CentralScreenState, innerPadding: PaddingValues) {
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(MaterialTheme.colorsResource.cian)) {

        }
    }


}