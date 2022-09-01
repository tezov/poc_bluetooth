package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.StringHex
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.ByteTo
import com.tezov.bluetooth.external_lib_kotlin.util.UtilsBytes
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.LongTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo

object LongTo {
    var BYTES = java.lang.Long.SIZE / ByteTo.SIZE
    fun Bytes(l: Long): ByteArray {
        var l = l
        val result = UtilsBytes.obtain(BYTES)
        for (i in BYTES - 1 downTo 0) {
            result[i] = (l and 0xFF).toByte()
            l = l shr ByteTo.SIZE
        }
        return result
    }

    fun StringHex(l: Long?): String? {
        return if (l == null) {
            null
        } else StringHex(Bytes(l))
    }
}