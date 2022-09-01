package com.tezov.bluetooth.external_lib_kotlin.type.primitive

object BooleanTo {
    fun Byte(b: Boolean): Byte {
        return if (b) 1.toByte() else 0.toByte()
    }

    fun Bytes(b: Boolean): ByteArray {
        return byteArrayOf(if (b) 1.toByte() else 0.toByte())
    }
}