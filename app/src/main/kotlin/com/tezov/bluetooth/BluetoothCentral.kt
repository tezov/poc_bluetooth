package com.tezov.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import com.tezov.bluetooth.BluetoothManager.Companion.MAX_MTU_SIZE_byte
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

@SuppressLint("MissingPermission")
class BluetoothCentral(
    private val manager: BluetoothManager,
) {
    var request = Request.IDLE
        get() {
            synchronized(this@BluetoothCentral) {
                return field
            }
        }
        private set
    private var requestSuspendContinuation: Continuation<Unit>? = null
    private var gatt: BluetoothGatt? = null

    private var _isScanning = false
    var isScanning = _isScanning
        get() {
            synchronized(this@BluetoothCentral) {
                return _isScanning
            }
        }
        private set(value) {
            _isScanning = value
            field = value
        }
    val isGattConnected: Boolean
        get() {
            synchronized(this@BluetoothCentral) {
                return gatt != null
            }
        }

    enum class Request {
        IDLE,
        SCAN_START,
        SCAN_STOP,
        GATT_CONNECT,
        GATT_DISCONNECT,
        GATT_DISCOVERY,
        GATT_SET_MTU,
    }

    private fun ensureNoSuspendInProgress() {
        if (requestSuspendContinuation != null) {
            throw IllegalStateException("try to start a suspend without waiting the completion of the current operation")
        }
    }

    private fun ensureNoRequestInProgress() {
        if (request != Request.IDLE) {
            throw IllegalStateException("try to start another request without waiting the completion of the current request")
        }
    }

    fun startScan(
        servicesUuid: List<ParcelUuid>,
    ): Flow<BluetoothDevice> {
        val filters: MutableList<ScanFilter> = ArrayList()
        servicesUuid.forEach {
            val filter = ScanFilter.Builder().apply {
                setServiceUuid(it)
            }.build()
            filters.add(filter)
        }
        return startScan(filters = filters)
    }

    fun startScan(
        scanSettings: ScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .build(),
        filters: List<ScanFilter>? = null
    ) = callbackFlow {

        var callback by Delegates.notNull<ScanCallback>()
        synchronized(this@BluetoothCentral) {
            ensureNoRequestInProgress()
            request = Request.SCAN_START
            callback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    synchronized(this@BluetoothCentral) {
                        if (request == Request.SCAN_STOP) {
                            channel.close()
                        } else {
                            result.device.takeIf { it.address != null }?.let {
                                channel.trySend(it)
                            }
                        }
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    close(Throwable("$errorCode"))
                }
            }
            isScanning = true
            request = Request.IDLE
            manager.adapter.bluetoothLeScanner.startScan(filters, scanSettings, callback)
        }
        awaitClose {
            synchronized(this@BluetoothCentral) {
                manager.adapter.bluetoothLeScanner.stopScan(callback)
                isScanning = false
                requestSuspendContinuation?.let {
                    requestSuspendContinuation = null
                    if (request == Request.SCAN_STOP) {
                        request = Request.IDLE
                    }
                    it.resumeWith(Result.success(Unit))
                }
            }
        }
    }

    suspend fun stopScan() = suspendCoroutine { continuation ->
        synchronized(this@BluetoothCentral) {
            ensureNoRequestInProgress()
            ensureNoSuspendInProgress()
            if (_isScanning) {
                requestSuspendContinuation = continuation
                request = Request.SCAN_STOP
            } else {
                continuation.resumeWith(Result.success(Unit))
            }
        }
    }

    suspend fun connectGatt(context: Context, device: BluetoothDevice) {
        stopScan()
        suspendCoroutine { continuation ->
            var callback by Delegates.notNull<BluetoothGattCallback>()
            synchronized(this@BluetoothCentral) {
                ensureNoRequestInProgress()
                ensureNoSuspendInProgress()
                callback = object : BluetoothGattCallback() {
                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt,
                        status: Int,
                        newState: Int
                    ) {
                        this@BluetoothCentral.onConnectionStateChange(gatt, status, newState)
                    }

                    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                        this@BluetoothCentral.onServicesDiscovered(gatt, status)
                    }

                    override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
                        this@BluetoothCentral.onMtuChanged(gatt, mtu, status)
                    }

                    override fun onCharacteristicRead(
                        gatt: BluetoothGatt,
                        characteristic: BluetoothGattCharacteristic,
                        status: Int
                    ) {
                        this@BluetoothCentral.onCharacteristicRead(gatt, characteristic, status)
                    }

                    override fun onCharacteristicWrite(
                        gatt: BluetoothGatt,
                        characteristic: BluetoothGattCharacteristic,
                        status: Int
                    ) {
                        this@BluetoothCentral.onCharacteristicWrite(gatt, characteristic, status)
                    }

                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt,
                        characteristic: BluetoothGattCharacteristic
                    ) {
                        this@BluetoothCentral.onCharacteristicChanged(gatt, characteristic)
                    }

                    override fun onDescriptorRead(
                        gatt: BluetoothGatt?,
                        descriptor: BluetoothGattDescriptor?,
                        status: Int
                    ) {
                        this@BluetoothCentral.onDescriptorRead(gatt, descriptor, status)
                    }

                    override fun onDescriptorWrite(
                        gatt: BluetoothGatt?,
                        descriptor: BluetoothGattDescriptor?,
                        status: Int
                    ) {
                        this@BluetoothCentral.onDescriptorWrite(gatt, descriptor, status)
                    }

                }
                requestSuspendContinuation = continuation
                request = Request.GATT_CONNECT
                device.connectGatt(context, false, callback)
            }
        }
    }

    suspend fun disconnectGatt() = suspendCoroutine { continuation ->
        synchronized(this@BluetoothCentral) {
            ensureNoRequestInProgress()
            ensureNoSuspendInProgress()
            gatt?.let {
                requestSuspendContinuation = continuation
                request = Request.GATT_DISCONNECT
                it.disconnect()
            } ?: run {
                continuation.resumeWith(Result.success(Unit))
            }
        }
    }

    suspend fun discovery() = suspendCoroutine { continuation ->
        synchronized(this@BluetoothCentral) {
            ensureNoRequestInProgress()
            ensureNoSuspendInProgress()
            gatt?.takeIf { isGattConnected }?.let {
                requestSuspendContinuation = continuation
                request = Request.GATT_DISCOVERY
                it.discoverServices()
            } ?: run {
                continuation.resumeWith(Result.failure(Throwable("gatt not connected")))
            }
        }
    }

    fun services(): List<BluetoothGattService> = gatt?.services ?: emptyList()
    fun BluetoothGatt.logServices() {
        Log.d(">>:", "Discovered ${services.size} services for ${device.address}")
        if (services.isEmpty()) {
            Log.d(
                ">>:",
                "No service and characteristic available"
            )
        } else {
            services.forEach { service ->
                val characteristicsTable = service.characteristics.joinToString(
                    separator = "\n|--",
                    prefix = "|--"
                ) { it.uuid.toString() }
                Log.d(
                    ">>:",
                    "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
                )
            }
        }
    }

    suspend fun setMtu(size: Int = MAX_MTU_SIZE_byte) = suspendCoroutine { continuation ->
        synchronized(this@BluetoothCentral) {
            gatt?.takeIf { isGattConnected }?.let {
                ensureNoRequestInProgress()
                ensureNoSuspendInProgress()
                requestSuspendContinuation = continuation
                request = Request.GATT_SET_MTU
                it.requestMtu(size)
            } ?: run {
                continuation.resumeWith(Result.failure(Throwable("gatt not connected")))
            }
        }
    }

    private fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        synchronized(this@BluetoothCentral) {
            requestSuspendContinuation?.let {
                requestSuspendContinuation = null
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    when {
                        (request == Request.GATT_CONNECT) && (newState == BluetoothProfile.STATE_CONNECTED) -> {
                            this@BluetoothCentral.gatt = gatt
                            request = Request.IDLE
                            it.resumeWith(Result.success(Unit))
                        }
                        (request == Request.GATT_DISCONNECT) && (newState == BluetoothProfile.STATE_DISCONNECTED) -> {
                            this@BluetoothCentral.gatt = null
                            request = Request.IDLE
                            it.resumeWith(Result.success(Unit))
                        }
                        else -> {
                            it.resumeWith(Result.failure(Throwable("GATT_SUCCESS unknown request ${request.name} or newState $newState")))
                        }
                    }
                } else {
                    when (request) {
                        Request.GATT_CONNECT, Request.GATT_DISCONNECT -> {
                            request = Request.IDLE
                            it.resumeWith(Result.failure(Throwable("${request.name} with error code $status")))
                        }
                        else -> {
                            it.resumeWith(Result.failure(Throwable("GATT_FAILURE unknown request ${request.name}")))
                        }
                    }
                }
            } ?: run {
                Log.d(
                    ">>",
                    "GATT STATE CHANGE without request, status = $status newState = $newState"
                ) //todo
            }
        }
    }

    private fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        synchronized(this@BluetoothCentral) {
            if (request == Request.GATT_DISCOVERY) {
                requestSuspendContinuation?.let {
                    requestSuspendContinuation = null
                    request = Request.IDLE
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        it.resumeWith(Result.success(Unit))
                    } else {
                        it.resumeWith(Result.failure(Throwable("gatt discovery with error code ${status}")))
                    }
                }
            }
        }
    }

    private fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
        synchronized(this@BluetoothCentral) {
            if (request == Request.GATT_SET_MTU) {
                requestSuspendContinuation?.let {
                    requestSuspendContinuation = null
                    request = Request.IDLE
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        it.resumeWith(Result.success(Unit))
                    } else {
                        it.resumeWith(Result.failure(Throwable("gatt mtu change with error code ${status}")))
                    }
                }
            }
        }
    }

    private fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {

    }

    private fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {

    }

    private fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {

    }

    private fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {

    }

    private fun onDescriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {

    }


    //    private fun requestReadBatteryLevel() {
//        bluetoothGatt?.run {
//            Log.d(">>:", "requestReadBatteryLevel for ${device.address}")
//            val batteryServiceUuid = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
//            val batteryLevelCharUuid = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
//            val batteryLevelChar =
//                getService(batteryServiceUuid)?.getCharacteristic(batteryLevelCharUuid)
//            if (batteryLevelChar?.isReadable() == true) {
//                readCharacteristic(batteryLevelChar)
//            } else {
//                Log.d(">>:", "battery level not readable")
//            }
//        }
//
//    }
//
//    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, payload: ByteArray) {
//        val writeType = when {
//            characteristic.isWritable() -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
//            characteristic.isWritableWithoutResponse() -> {
//                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
//            }
//            else -> error("Characteristic ${characteristic.uuid} cannot be written to")
//        }
//
//        bluetoothGatt?.let { gatt ->
//            characteristic.writeType = writeType
//            characteristic.value = payload
//            gatt.writeCharacteristic(characteristic)
//        } ?: error("Not connected to a BLE device!")
//    }
//
//    fun enableNotifications(characteristic: BluetoothGattCharacteristic) {
//        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
//        val payload = when {
//            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
//            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//            else -> {
//                Log.e(
//                    "ConnectionManager",
//                    "${characteristic.uuid} doesn't support notifications/indications"
//                )
//                return
//            }
//        }
//
//        characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
//            if (bluetoothGatt?.setCharacteristicNotification(characteristic, true) == false) {
//                Log.e(
//                    "ConnectionManager",
//                    "setCharacteristicNotification failed for ${characteristic.uuid}"
//                )
//                return
//            }
//            writeDescriptor(cccDescriptor, payload)
//        } ?: Log.e(
//            "ConnectionManager",
//            "${characteristic.uuid} doesn't contain the CCC descriptor!"
//        )
//    }
//
//    fun disableNotifications(characteristic: BluetoothGattCharacteristic) {
//        if (!characteristic.isNotifiable() && !characteristic.isIndicatable()) {
//            Log.e(
//                "ConnectionManager",
//                "${characteristic.uuid} doesn't support indications/notifications"
//            )
//            return
//        }
//
//        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
//        characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
//            if (bluetoothGatt?.setCharacteristicNotification(characteristic, false) == false) {
//                Log.e(
//                    "ConnectionManager",
//                    "setCharacteristicNotification failed for ${characteristic.uuid}"
//                )
//                return
//            }
//            writeDescriptor(cccDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
//        } ?: Log.e(
//            "ConnectionManager",
//            "${characteristic.uuid} doesn't contain the CCC descriptor!"
//        )
//    }


    //    @SuppressLint("MissingPermission")
//    private fun requestConnectGatt(device: BluetoothDevice) {
//        bluetoothGatt?.run {
//            requestDisconnectGatt()
//        }
//        Log.d(">>:", "requestConnectGatt for ${device.address}")
//        bluetoothGattCallback = object : BluetoothGattCallback() {

//            override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
//                Log.d(
//                    ">>:",
//                    "ATT MTU changed to $mtu, success: ${status == BluetoothGatt.GATT_SUCCESS}"
//                )
//            }
//
//            override fun onCharacteristicRead(
//                gatt: BluetoothGatt,
//                characteristic: BluetoothGattCharacteristic,
//                status: Int
//            ) {
//                with(characteristic) {
//                    when (status) {
//                        BluetoothGatt.GATT_SUCCESS -> {
//                            Log.d(">>:", "Read characteristic $uuid:\n${value.toHexString()}")
//                        }
//                        BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
//                            Log.d(">>:", "Read not permitted for $uuid!")
//                        }
//                        else -> {
//                            Log.d(">>:", "Characteristic read failed for $uuid, error: $status")
//                        }
//                    }
//                }
//            }
//
//            override fun onCharacteristicWrite(
//                gatt: BluetoothGatt,
//                characteristic: BluetoothGattCharacteristic,
//                status: Int
//            ) {
//                with(characteristic) {
//                    when (status) {
//                        BluetoothGatt.GATT_SUCCESS -> {
//                            Log.i(
//                                "BluetoothGattCallback",
//                                "Wrote to characteristic $uuid | value: ${value.toHexString()}"
//                            )
//                        }
//                        BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
//                            Log.e("BluetoothGattCallback", "Write exceeded connection ATT MTU!")
//                        }
//                        BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
//                            Log.e("BluetoothGattCallback", "Write not permitted for $uuid!")
//                        }
//                        else -> {
//                            Log.e(
//                                "BluetoothGattCallback",
//                                "Characteristic write failed for $uuid, error: $status"
//                            )
//                        }
//                    }
//                }
//            }
//
//            override fun onCharacteristicChanged(
//                gatt: BluetoothGatt,
//                characteristic: BluetoothGattCharacteristic
//            ) {
//                with(characteristic) {
//                    Log.i(
//                        "BluetoothGattCallback",
//                        "Characteristic $uuid changed | value: ${value.toHexString()}"
//                    )
//                }
//            }
//
//        }
//        device.connectGatt(this, false, bluetoothGattCallback)
//    }


}