package com.tezov.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import com.tezov.bluetooth.external_lib_android.ui.activity.ActivityBase

@SuppressLint("MissingPermission")
class BluetoothManager(
    val manager: BluetoothManager,
) {

    val adapter: BluetoothAdapter get() = manager.adapter
    val isEnabled: Boolean get() = adapter.isEnabled

    companion object {
        val MAX_MTU_SIZE_byte = 517

        fun BluetoothGattCharacteristic.isReadable() =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)

        fun BluetoothGattCharacteristic.isWritable() =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE)

        fun BluetoothGattCharacteristic.isWritableWithoutResponse() =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

        fun BluetoothGattCharacteristic.containsProperty(property: Int) =
            (properties and property) != 0

        fun BluetoothGattDescriptor.containsPermission(permission: Int): Boolean =
            permissions and permission != 0

        fun BluetoothGattDescriptor.isReadable(): Boolean =
            containsPermission(BluetoothGattDescriptor.PERMISSION_READ) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM)

        fun BluetoothGattDescriptor.isWritable(): Boolean =
            containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED) ||
                    containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM)

        fun BluetoothGattCharacteristic.isIndicatable() =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

        fun BluetoothGattCharacteristic.isNotifiable() =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

    }

    suspend fun enable(activity: ActivityBase): ActivityBase.RequestForResult.Response {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        return ActivityBase.RequestForResult(activity, intent).response()
    }

    fun disable()  = manager.adapter.disable()

    fun openServer(context: Context, callback: BluetoothGattServerCallback) = manager.openGattServer(context, callback)



}