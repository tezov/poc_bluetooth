package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.StringHex
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.ByteTo
import com.tezov.bluetooth.external_lib_kotlin.util.UtilsBytes
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.IntTo
import kotlin.jvm.JvmOverloads
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringHexTo

object IntTo {
    var BYTES = Integer.SIZE / ByteTo.SIZE
    var MAX_DIGIT_POSITIVE = Integer.toString(Int.MAX_VALUE).length
    var MAX_DIGIT_NEGATIVE = Integer.toString(Int.MIN_VALUE).length - 1
    fun Bytes(i: Int): ByteArray {
        var i = i
        val result = UtilsBytes.obtain(BYTES)
        for (j in BYTES - 1 downTo 0) {
            result[j] = (i and 0xFF).toByte()
            i = i shr ByteTo.SIZE
        }
        return result
    }

    @JvmOverloads
    fun StringHex(i: Int?, addPrefix: Boolean = false): String? {
        return if (i == null) {
            null
        } else if (!addPrefix) {
            StringHex(Bytes(i))
        } else {
            StringHexTo.HEX_PREFIX + StringHex(Bytes(i))
        }
    }
}