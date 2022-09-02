package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringCharTo.decode
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringHexTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringHexCharTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringBase64To
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringBase58To
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringBase49To
import com.tezov.bluetooth.external_lib_kotlin.util.UtilsBytes
import kotlin.experimental.and

object BytesTo {
    fun Chars(b: ByteArray?): CharArray? {
        if (b == null) {
            return null
        }
        val c = CharArray(b.size)
        for (i in b.indices) {
            c[i] = (b[i].toInt().toChar().code and 0x00FF).toChar()
        }
        return c
    }

    fun Long(b: ByteArray?, offset: Int = 0): Long? {
        if (b == null) {
            return null
        }
        var result: Long = 0
        val end = Math.min(b.size - offset, LongTo.BYTES)
        var i = 0
        while (i < end) {
            result = result shl ByteTo.SIZE
            result = result or (b[i + offset] and 0xFF.toByte()).toLong()
            i++
        }
        return result
    }

    fun Double(b: ByteArray?, offset: Int = 0): Double? {
        if (b == null) {
            return null
        }
        if (b.size - offset < java.lang.Double.BYTES) {
            return null
        }
        var result = 0L
        result = result or (b[offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[1 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[2 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[3 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[4 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[5 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[6 + offset] and 0xFF.toByte()).toLong()
        result = result shl ByteTo.SIZE
        result = result or (b[7 + offset] and 0xFF.toByte()).toLong()
        return java.lang.Double.longBitsToDouble(result)
    }

    fun Int(b: ByteArray?, offset: Int = 0): Int? {
        if (b == null) {
            return null
        }
        var result = 0
        val end = Math.min(b.size - offset, IntTo.BYTES)
        var i = 0
        while (i < end) {
            result = result shl ByteTo.SIZE
            result = result or (b[i + offset] and 0xFF.toByte()).toInt()
            i++
        }
        return result
    }

    fun Float(b: ByteArray?, offset: Int = 0): Float? {
        if (b == null) {
            return null
        }
        if (b.size - offset < FloatTo.BYTES) {
            return null
        }
        var result = 0
        result = result or (b[offset] and 0xFF.toByte()).toInt()
        result = result shl ByteTo.SIZE
        result = result or (b[1 + offset] and 0xFF.toByte()).toInt()
        result = result shl ByteTo.SIZE
        result = result or (b[2 + offset] and 0xFF.toByte()).toInt()
        result = result shl ByteTo.SIZE
        result = result or (b[3 + offset] and 0xFF.toByte()).toInt()
        return java.lang.Float.intBitsToFloat(result)
    }

    fun Boolean(b: ByteArray?, offset: Int = 0): Boolean? {
        if (b == null) {
            return null
        }
        return if (b.size - offset < ByteTo.BYTES) {
            null
        } else b[offset] == 1.toByte()
    }

    fun StringHex(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            StringHexTo.decode(b)
        }
    }

    fun StringHexChar(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            StringHexCharTo.decode(b)
        }
    }

    fun StringChar(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            decode(b)
        }
    }

    fun StringBase64(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            StringBase64To.encode(b)
        }
    }

    fun StringBase58(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            StringBase58To.encode(b)
        }
    }

    fun StringBase49(b: ByteArray?): String? {
        return if (b == null) {
            null
        } else {
            StringBase49To.encode(b)
        }
    }

    fun complement(bytes: ByteArray?): ByteArray {
        return UtilsBytes.xor(bytes, 0xff.toByte())
    }
}