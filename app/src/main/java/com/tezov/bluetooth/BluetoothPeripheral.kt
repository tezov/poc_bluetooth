package com.tezov.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

@SuppressLint("MissingPermission")
class BluetoothPeripheral(
    val manager: BluetoothManager,
) {
    var request = Request.IDLE
        get() {
            synchronized(this@BluetoothPeripheral) {
                return field
            }
        }
        private set
    private var requestSuspendContinuation: Continuation<Unit>? = null

    private var server: BluetoothGattServer? = null
    private var callbackAdvertising: AdvertiseCallback? = null
    private var gatt: List<BluetoothGatt> = ArrayList()

    val isOpened: Boolean
        get() {
            synchronized(this@BluetoothPeripheral) {
                return server != null
            }
        }

    private var _isAdvertissing = false
    var isAdvertissing = _isAdvertissing
        get() {
            synchronized(this@BluetoothPeripheral) {
                return _isAdvertissing
            }
        }
        private set(value) {
            _isAdvertissing = value
            field = value
        }

    val isGattConnected: Boolean
        get() {
            synchronized(this@BluetoothPeripheral) {
                return gatt.isNotEmpty()
            }
        }

    enum class Request {
        IDLE,
        SERVER_OPEN,
        SERVER_CLOSE,
        ADVERTISE_START,
        ADVERTISE_STOP,
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

    private fun ensureServicesNotEmpty(services: List<BluetoothGattService>) {
        if (services.isEmpty()) {
            throw IllegalStateException("try to open server with empty service list")
        }
    }

    fun open(context: Context, services: List<BluetoothGattService>): Boolean {
        var callback by Delegates.notNull<BluetoothGattServerCallback>()
        synchronized(this@BluetoothPeripheral) {
            ensureNoRequestInProgress()
            ensureServicesNotEmpty(services)
            callback = object : BluetoothGattServerCallback() {

                override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
                    this@BluetoothPeripheral.onServiceAdded(status, service)
                }

                override fun onMtuChanged(device: BluetoothDevice?, mtu: Int) {
                    this@BluetoothPeripheral.onMtuChanged(device, mtu)

                }

                override fun onConnectionStateChange(
                    device: BluetoothDevice?,
                    status: Int,
                    newState: Int
                ) {
                    this@BluetoothPeripheral.onConnectionStateChange(device, status, newState)
                }

                override fun onCharacteristicReadRequest(
                    device: BluetoothDevice?,
                    requestId: Int,
                    offset: Int,
                    characteristic: BluetoothGattCharacteristic?
                ) {
                    this@BluetoothPeripheral.onCharacteristicReadRequest(
                        device,
                        requestId,
                        offset,
                        characteristic
                    )
                }

                override fun onCharacteristicWriteRequest(
                    device: BluetoothDevice?,
                    requestId: Int,
                    characteristic: BluetoothGattCharacteristic?,
                    preparedWrite: Boolean,
                    responseNeeded: Boolean,
                    offset: Int,
                    value: ByteArray?
                ) {
                    this@BluetoothPeripheral.onCharacteristicWriteRequest(
                        device,
                        requestId,
                        offset,
                        characteristic,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        value
                    )
                }

                override fun onDescriptorReadRequest(
                    device: BluetoothDevice?,
                    requestId: Int,
                    offset: Int,
                    descriptor: BluetoothGattDescriptor?
                ) {
                    this@BluetoothPeripheral.onDescriptorReadRequest(
                        device,
                        requestId,
                        offset,
                        descriptor
                    )

                }

                override fun onDescriptorWriteRequest(
                    device: BluetoothDevice?,
                    requestId: Int,
                    descriptor: BluetoothGattDescriptor?,
                    preparedWrite: Boolean,
                    responseNeeded: Boolean,
                    offset: Int,
                    value: ByteArray?
                ) {
                    this@BluetoothPeripheral.onDescriptorWriteRequest(
                        device,
                        requestId,
                        offset,
                        descriptor,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        value
                    )
                }
            }
            request = Request.SERVER_OPEN
            manager.openServer(context, callback)?.let { server ->
                var allServicesAdded = true
                services.forEach {
                    if (!server.addService(it)) {
                        allServicesAdded = false
                        return@forEach
                    }
                }
                requestSuspendContinuation = null
                request = Request.IDLE
                if (allServicesAdded) {
                    this.server = server
                } else {
                    this.server = null
                }
            }
        }
        return server != null
    }

    fun close() {
        stopAdvertising()
        synchronized(this@BluetoothPeripheral) {
            ensureNoRequestInProgress()
            server?.let {
                request = Request.SERVER_CLOSE
                it.close()
                request = Request.IDLE
            }
        }
    }

    suspend fun startAdvertising(
        servicesUuid: List<ParcelUuid>,
    ) {
        val data = AdvertiseData.Builder().apply {
            setIncludeDeviceName(true)
            setIncludeTxPowerLevel(false)
            servicesUuid.forEach {
                addServiceUuid(it)
            }
        }.build()
        startAdvertising(data = data)
    }

    suspend fun startAdvertising(
        settings: AdvertiseSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .build(),
        data: AdvertiseData,
    ) = suspendCoroutine { continuation ->
        synchronized(this@BluetoothPeripheral) {
            ensureNoRequestInProgress()
            ensureNoSuspendInProgress()
            server?.let {
                requestSuspendContinuation = continuation
                request = Request.ADVERTISE_START
                callbackAdvertising = object : AdvertiseCallback() {
                    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                        onCompletion()
                    }

                    override fun onStartFailure(errorCode: Int) {
                        onCompletion(errorCode)
                    }

                    private fun onCompletion(errorCode: Int? = null) {
                        synchronized(this@BluetoothPeripheral) {
                            if (request == Request.ADVERTISE_START) {
                                requestSuspendContinuation?.let {
                                    requestSuspendContinuation = null
                                    request = Request.IDLE
                                    if (errorCode == null) {
                                        isAdvertissing = true
                                        it.resumeWith(Result.success(Unit))
                                    } else {
                                        isAdvertissing = false
                                        it.resumeWith(Result.failure(Throwable("advertive start failed with error code ${errorCode}")))
                                    }
                                }
                            }
                        }
                    }
                }
                manager.adapter.bluetoothLeAdvertiser.startAdvertising(
                    settings,
                    data,
                    callbackAdvertising
                )
            } ?: run {
                continuation.resumeWith(Result.failure(Throwable("server not open")))
            }
        }
    }

    fun stopAdvertising() {
        synchronized(this@BluetoothPeripheral) {
            ensureNoRequestInProgress()
            ensureNoSuspendInProgress()
            callbackAdvertising?.takeIf { _isAdvertissing }?.let {
                request = Request.ADVERTISE_STOP
                manager.adapter.bluetoothLeAdvertiser.stopAdvertising(it)
                isAdvertissing = false
                request = Request.IDLE
            }
        }

    }

    private fun onServiceAdded(status: Int, service: BluetoothGattService?) {

    }

    private fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
        synchronized(this@BluetoothPeripheral) {
            requestSuspendContinuation?.let {
                requestSuspendContinuation = null



                request = Request.IDLE
            } ?: run {
                Log.d(
                    ">>",
                    "GATT STATE CHANGE without request, status = $status newState = $newState"
                ) //todo
            }
        }
    }


    private fun onMtuChanged(device: BluetoothDevice?, mtu: Int) {

    }

    private fun onDescriptorWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        descriptor: BluetoothGattDescriptor?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset1: Int,
        value: ByteArray?
    ) {

    }

    private fun onDescriptorReadRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        descriptor: BluetoothGattDescriptor?
    ) {

    }

    private fun onCharacteristicWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        characteristic: BluetoothGattCharacteristic?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset1: Int,
        value: ByteArray?
    ) {

    }

    private fun onCharacteristicReadRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        characteristic: BluetoothGattCharacteristic?
    ) {

    }


}