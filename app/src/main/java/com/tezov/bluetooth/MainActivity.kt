package com.tezov.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.provider.Settings
import android.util.Log
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
import com.tezov.bluetooth.external_lib_android.application.AppPermission
import com.tezov.bluetooth.external_lib_android.ui.activity.ActivityBase
import com.tezov.bluetooth.ui.theme.ThemeApplication
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlin.properties.Delegates

@SuppressLint("MissingPermission")
class MainActivity : ActivityBase() {
    private var hasAllPermissionsNeededGranted by Delegates.notNull<MutableState<Boolean>>()

    private val bluetoothManager by lazy {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
        BluetoothManager(bluetoothManager)
    }
    private val bluetoothCentral by lazy {
        BluetoothCentral(bluetoothManager)
    }
    private val bluetoothPeripheral by lazy {
        BluetoothPeripheral(bluetoothManager)
    }

    private var bluetoothEnabledState by Delegates.notNull<MutableState<Boolean>>()
    private var bluetoothScanJob by Delegates.notNull<MutableState<Job?>>()
    private var deviceScanList by Delegates.notNull<MutableMap<String, BluetoothDevice>>()

    private var bluetoothServerOpenedState by Delegates.notNull<MutableState<Boolean>>()
    private var bluetoothAdvertisingStarted by Delegates.notNull<MutableState<Boolean>>()

    private fun getBluetoothMac(context: Context): String? {
        return null
    }


    @Composable
    override fun Content() {
        hasAllPermissionsNeededGranted = remember {
            mutableStateOf(checkPermissions())
        }
        bluetoothEnabledState = remember {
            mutableStateOf(bluetoothManager.isEnabled)
        }
        bluetoothScanJob = remember {
            mutableStateOf(null)
        }
        deviceScanList = remember {
            mutableStateMapOf()
        }

        bluetoothServerOpenedState = remember {
            mutableStateOf(bluetoothPeripheral.isOpened)
        }
        bluetoothAdvertisingStarted = remember {
            mutableStateOf(bluetoothPeripheral.isAdvertissing)
        }

        ThemeApplication.Bluetooth {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth()
                        .background(Color.Black))
                    Text(text="my address ${getBluetoothMac(this@MainActivity)}")
                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth()
                        .background(Color.Black))


                    ButtonOnOff(
                        text = "1- requestPermissions()",
                        color = {
                            when (hasAllPermissionsNeededGranted.value) {
                                false -> false
                                true -> true
                            }
                        },
                        enable = { true },
                        onClick = { if (!hasAllPermissionsNeededGranted.value) requestPermissions() })
                    ButtonOnOff(
                        text = "2- requestToggleBluetooth()",
                        color = {
                            when (bluetoothEnabledState.value) {
                                false -> false
                                true -> true
                            }
                        },
                        enable = { hasAllPermissionsNeededGranted.value },
                        onClick = { requestToggleBluetooth() })

                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth()
                        .background(Color.Black))

                    ButtonOnOff(
                        text = "3- requestToggleServer()",
                        color = {
                            when (bluetoothServerOpenedState.value) {
                                false -> false
                                true -> true
                            }
                        },
                        enable = { hasAllPermissionsNeededGranted.value && bluetoothEnabledState.value },
                        onClick = { requestToggleServerBluetooth() })

                    ButtonOnOff(
                        text = "4- requestToggleAdvertising()",
                        color = {
                            when (bluetoothAdvertisingStarted.value) {
                                false -> false
                                true -> true
                            }
                        },
                        enable = { hasAllPermissionsNeededGranted.value && bluetoothEnabledState.value && bluetoothServerOpenedState.value},
                        onClick = { requestToggleAdvertisingBluetooth() })

                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth()
                        .background(Color.Black))

                    ButtonOnOff(
                        text = "3- requestToggleScan()",
                        color = {
                            when (bluetoothScanJob.value) {
                                null -> false
                                else -> true
                            }
                        },
                        enable = { hasAllPermissionsNeededGranted.value && bluetoothEnabledState.value },
                        onClick = { requestToggleScanBluetooth() })
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        val keys = deviceScanList.keys.toList()
                        items(count = keys.size){ index ->
                            val key = keys[index]
                            val item = deviceScanList[key]
                            item?.let {
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Color.Black))
                                Card(modifier = Modifier.padding(6.dp)) {
                                    Text(text = "${it.name} -> ${it.address}", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ButtonOnOff(
        text: String,
        enable: () -> Boolean,
        color: () -> Boolean,
        onClick: () -> Unit
    ) {
        Button(
            modifier = Modifier.padding(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = when (color()) {
                    false -> Color.Gray
                    true -> Color.Green
                }
            ),
            enabled = enable(),
            onClick = onClick
        ) {
            Text(
                text = text,
                fontSize = 18.sp
            )
        }
    }


    private fun permissions() = ArrayList<String>().apply {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        add(Manifest.permission.BLUETOOTH_ADMIN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_CONNECT)
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_ADVERTISE)
        }
    }

    private fun checkPermissions() = AppPermission.Checker(this@MainActivity).apply {
        add(permissions())
    }.check().isAllGranted()

    private fun requestPermissions() {
        lifecycleScope.launch(Dispatchers.IO) {
            hasAllPermissionsNeededGranted.value = false
            AppPermission.Checker(this@MainActivity).apply {
                add(permissions())
                val response = response()
                withContext(Dispatchers.Main) {
                    if (response.isAllGranted()) {
                        hasAllPermissionsNeededGranted.value = true
                    } else {
                        hasAllPermissionsNeededGranted.value = false
                    }
                }
            }
        }
    }

    private fun requestToggleBluetooth() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.d(">>:", "$exception")
            bluetoothScanJob.value = null
            bluetoothEnabledState.value = false
        }
        lifecycleScope.launch(Dispatchers.IO + exceptionHandler) {
            if (bluetoothEnabledState.value) {
                if (bluetoothCentral.isGattConnected) {
                    bluetoothCentral.disconnectGatt()
                }
                if (bluetoothCentral.isScanning) {
                    bluetoothCentral.stopScan()
                }
                bluetoothManager.disable()
                bluetoothScanJob.value = null
                bluetoothEnabledState.value = false
            } else {
                val response = bluetoothManager.enable(this@MainActivity)
                withContext(Dispatchers.Main) {
                    if (response.isOk()) {
                        bluetoothEnabledState.value = true
                    }
                }
            }
        }
    }

    val service:BluetoothGattService = BluetoothTimeProfile.createTimeService()
    val serviceUuid = ParcelUuid(BluetoothTimeProfile.TIME_SERVICE)

    private fun requestToggleScanBluetooth() {
        bluetoothScanJob.value?.let {
            it.cancelChildren()
            bluetoothScanJob.value = null
        } ?: run {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Log.d(">>:", "$exception")
                bluetoothScanJob.value?.cancelChildren()
                bluetoothScanJob.value = null
            }
            bluetoothScanJob.value = lifecycleScope.launch(Dispatchers.IO + exceptionHandler) {
                deviceScanList.clear()
                bluetoothCentral.startScan(listOf(serviceUuid)).catch {
                    Log.d(">>:", "error")
                }.conflate().collect {
                    deviceScanList.put(it.address, it)
                }
            }
        }
    }

    private fun requestToggleServerBluetooth() {
        if (bluetoothServerOpenedState.value) {
            bluetoothPeripheral.close()
            bluetoothAdvertisingStarted.value = false
            bluetoothServerOpenedState.value = false
        } else {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Log.d(">>:", "$exception")
                bluetoothAdvertisingStarted.value = false
                bluetoothServerOpenedState.value = false
            }
            lifecycleScope.launch(Dispatchers.IO + exceptionHandler) {
                bluetoothServerOpenedState.value = bluetoothPeripheral.open(
                    this@MainActivity.applicationContext,
                    listOf(service)
                )
                if (!bluetoothServerOpenedState.value) {
                    throw Throwable("failed to open server with these services")
                }
            }
        }
    }

    private fun requestToggleAdvertisingBluetooth() {
        if (bluetoothAdvertisingStarted.value) {
            bluetoothPeripheral.stopAdvertising()
            bluetoothAdvertisingStarted.value = false
        } else {
            val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                Log.d(">>:", "$exception")
                bluetoothAdvertisingStarted.value = false
            }
            lifecycleScope.launch(Dispatchers.IO + exceptionHandler) {
                bluetoothPeripheral.startAdvertising(servicesUuid = listOf(serviceUuid))
                bluetoothAdvertisingStarted.value = true
            }

        }


    }


//    fun byteArray4ToInt(fourBytes: ByteArray) = (fourBytes[3].toInt() and 0xFF shl 24) +
//            (fourBytes[2].toInt() and 0xFF shl 16) +
//            (fourBytes[1].toInt() and 0xFF shl 8) +
//            (fourBytes[0].toInt() and 0xFF)

}

