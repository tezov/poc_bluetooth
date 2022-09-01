package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.StringHex
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.ByteTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.FloatTo

object FloatTo {
    var BYTES = java.lang.Float.SIZE / ByteTo.SIZE
    var MAX_DIGIT_DECIMAL = 7
    fun Bytes(f: Float): ByteArray {
        val intBits = java.lang.Float.floatToIntBits(f)
        return byteArrayOf(
            (intBits shr 24).toByte(),
            (intBits shr 16).toByte(),
            (intBits shr 8).toByte(),
            intBits.toByte()
        )
    }

    fun StringHex(f: Float?): String? {
        return if (f == null) {
            null
        } else StringHex(Bytes(f))
    }
}