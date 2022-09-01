package com.tezov.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService

import java.util.Calendar
import java.util.UUID

object BluetoothTimeProfile {

    val TIME_SERVICE: UUID = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb")
    val CURRENT_TIME: UUID = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb")
    val LOCAL_TIME_INFO: UUID = UUID.fromString("00002a0f-0000-1000-8000-00805f9b34fb")
    val CLIENT_CONFIG: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    const val ADJUST_NONE: Byte = 0x0
    const val ADJUST_MANUAL: Byte = 0x1
    const val ADJUST_EXTERNAL: Byte = 0x2
    const val ADJUST_TIMEZONE: Byte = 0x4
    const val ADJUST_DST: Byte = 0x8

    private const val FIFTEEN_MINUTE_MILLIS = 900000
    private const val HALF_HOUR_MILLIS = 1800000

    private const val DAY_UNKNOWN: Byte = 0
    private const val DAY_MONDAY: Byte = 1
    private const val DAY_TUESDAY: Byte = 2
    private const val DAY_WEDNESDAY: Byte = 3
    private const val DAY_THURSDAY: Byte = 4
    private const val DAY_FRIDAY: Byte = 5
    private const val DAY_SATURDAY: Byte = 6
    private const val DAY_SUNDAY: Byte = 7

    private const val DST_STANDARD: Byte = 0x0
    private const val DST_HALF: Byte = 0x2
    private const val DST_SINGLE: Byte = 0x4
    private const val DST_DOUBLE: Byte = 0x8
    private const val DST_UNKNOWN = 0xFF.toByte()

    fun createTimeService(): BluetoothGattService {
        val service = BluetoothGattService(TIME_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY)

        val currentTime = BluetoothGattCharacteristic(CURRENT_TIME,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ)

        val configDescriptor = BluetoothGattDescriptor(CLIENT_CONFIG,
            BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE)
        currentTime.addDescriptor(configDescriptor)

        val localTime = BluetoothGattCharacteristic(LOCAL_TIME_INFO,
            BluetoothGattCharacteristic.PROPERTY_READ,
            BluetoothGattCharacteristic.PERMISSION_READ)

        service.addCharacteristic(currentTime)
        service.addCharacteristic(localTime)
        return service
    }

    fun getExactTime(timestamp: Long, adjustReason: Byte): ByteArray {
        val time = Calendar.getInstance()
        time.timeInMillis = timestamp

        val field = ByteArray(10)


        val year = time.get(Calendar.YEAR)
        field[0] = (year and 0xFF).toByte()
        field[1] = (year shr 8 and 0xFF).toByte()

        field[2] = (time.get(Calendar.MONTH) + 1).toByte()

        field[3] = time.get(Calendar.DATE).toByte()

        field[4] = time.get(Calendar.HOUR_OF_DAY).toByte()

        field[5] = time.get(Calendar.MINUTE).toByte()

        field[6] = time.get(Calendar.SECOND).toByte()

        field[7] = getDayOfWeekCode(time.get(Calendar.DAY_OF_WEEK))

        field[8] = (time.get(Calendar.MILLISECOND) / 256).toByte()

        field[9] = adjustReason

        return field
    }

    fun getLocalTimeInfo(timestamp: Long): ByteArray {
        val time = Calendar.getInstance()
        time.timeInMillis = timestamp

        val field = ByteArray(2)

        val zoneOffset = time.get(Calendar.ZONE_OFFSET) / FIFTEEN_MINUTE_MILLIS // 15 minute intervals
        field[0] = zoneOffset.toByte()

        val dstOffset = time.get(Calendar.DST_OFFSET) / HALF_HOUR_MILLIS // 30 minute intervals
        field[1] = getDstOffsetCode(dstOffset)

        return field
    }

    private fun getDayOfWeekCode(dayOfWeek: Int): Byte = when (dayOfWeek) {
        Calendar.MONDAY -> DAY_MONDAY
        Calendar.TUESDAY -> DAY_TUESDAY
        Calendar.WEDNESDAY -> DAY_WEDNESDAY
        Calendar.THURSDAY -> DAY_THURSDAY
        Calendar.FRIDAY -> DAY_FRIDAY
        Calendar.SATURDAY -> DAY_SATURDAY
        Calendar.SUNDAY -> DAY_SUNDAY
        else -> DAY_UNKNOWN
    }

    private fun getDstOffsetCode(rawOffset: Int): Byte = when (rawOffset) {
        0 -> DST_STANDARD
        1 -> DST_HALF
        2 -> DST_SINGLE
        4 -> DST_DOUBLE
        else -> DST_UNKNOWN
    }
}