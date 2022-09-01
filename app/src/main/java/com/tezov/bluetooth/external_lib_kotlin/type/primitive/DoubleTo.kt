package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.StringHex
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.ByteTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.DoubleTo

object DoubleTo {
    var BYTES = java.lang.Double.SIZE / ByteTo.SIZE
    fun Bytes(f: Double): ByteArray {
        val intBits = java.lang.Double.doubleToLongBits(f)
        return byteArrayOf(
            (intBits shr 56).toByte(),
            (intBits shr 48).toByte(),
            (intBits shr 40).toByte(),
            (intBits shr 32).toByte(),
            (intBits shr 24).toByte(),
            (intBits shr 16).toByte(),
            (intBits shr 8).toByte(),
            intBits.toByte()
        )
    }

    fun StringHex(f: Double?): String? {
        return if (f == null) {
            null
        } else StringHex(Bytes(f))
    }
}