package com.tezov.bluetooth.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.tezov.bluetooth.BluetoothCentral
import com.tezov.bluetooth.BluetoothManager
import com.tezov.bluetooth.BluetoothPeripheral
import com.tezov.bluetooth.BluetoothTimeProfile
import com.tezov.bluetooth.external_lib_android.application.AppPermission
import com.tezov.bluetooth.external_lib_android.ui.activity.ActivityBase
import com.tezov.bluetooth.ui.navigation.NavigationGraph
import com.tezov.bluetooth.ui.navigation.bottom_navigation.BottomNavigationScreen
import com.tezov.bluetooth.ui.navigation.top_app_bar.TopAppBarItem
import com.tezov.bluetooth.ui.navigation.top_app_bar.TopAppBarScreen
import com.tezov.bluetooth.ui.screen.misc.bottomsheet.BottomSheetScreen
import com.tezov.bluetooth.ui.screen.misc.snackbar.SnackbarInfoScreen
import com.tezov.bluetooth.ui.theme.ThemeApplication
import com.tezov.shopoclone.ui.MainActivityState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlin.properties.Delegates

@SuppressLint("MissingPermission")
class MainActivity : ActivityBase() {


    @Composable
    override fun Content() {
        ThemeApplication.Bluetooth() {
            NavigationGraph(MainActivityState.remember())
        }
    }

    companion object Screen{
        @Composable
        fun withTopAppBarAndBottomNavigationBar(
            mainActivityState: MainActivityState,
            topAppBarTitleResourceId: Int,
            topAppBarLeadingItem: TopAppBarItem? = null,
            topAppBarTrailingItem: TopAppBarItem? = null,
            content: @Composable (PaddingValues) -> Unit
        ) {
            BottomSheetScreen(mainActivityState.screenStateProvider.bottomSheetScreenState()){
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    scaffoldState = mainActivityState.scaffoldState,
                    bottomBar = {
                        BottomNavigationScreen(state = mainActivityState.screenStateProvider.bottomNavigationScreenState())
                    },
                    topBar = {
                        TopAppBarScreen(
                            state = mainActivityState.screenStateProvider.topAppBarScreenState(),
                            topAppBarTitleResourceId,
                            topAppBarLeadingItem,
                            topAppBarTrailingItem
                        )
                    },
                    snackbarHost = {
                        SnackbarInfoScreen(mainActivityState.scaffoldState.snackbarHostState)
                    },
                    content = content
                )
            }
        }

        @Composable
        fun withBottomNavigationBar(
            mainActivityState: MainActivityState,
            content: @Composable (PaddingValues) -> Unit
        ) {
            BottomSheetScreen(mainActivityState.screenStateProvider.bottomSheetScreenState()){
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    scaffoldState = mainActivityState.scaffoldState,
                    bottomBar = {
                        BottomNavigationScreen(state = mainActivityState.screenStateProvider.bottomNavigationScreenState())
                    },
                    topBar = { },
                    snackbarHost = {
                        SnackbarInfoScreen(mainActivityState.scaffoldState.snackbarHostState)
                    },
                    content = content
                )
            }
        }
    }
}

