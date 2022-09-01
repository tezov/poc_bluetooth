package com.tezov.bluetooth.external_lib_kotlin.type.primitive.string

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.Int
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.StringChar
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo.complement
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringHexCharTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import java.nio.charset.StandardCharsets
import kotlin.experimental.and

object StringHexCharTo {
    val HEX_CHAR_ARRAY = "ABCDEFGHIJKLMNOP".toByteArray(StandardCharsets.UTF_8)
    const val HEX_PREFIX = "0x"
    private fun digit(c: Char): Int {
        return if ('A' <= c && c <= 'P') {
            c - 'A'
        } else {
            -1
        }
    }

    fun encode(s: String): ByteArray? {
        var s = s
        if (s.startsWith(HEX_PREFIX)) {
            s = s.substring(2)
        }
        if (s.length == 0) {
            return null
        }
        val len = s.length
        val parity = len % 2
        val data = ByteArray(len / 2 + parity)
        val end = len - parity
        var i = 0
        while (i < end) {
            data[i ushr 1] = (digit(s[i + 1]) + (digit(s[i]) shl 4)).toByte()
            i += 2
        }
        if (end < len) {
            data[end ushr 2] = (digit(s[end]) shl 4).toByte()
        }
        return data
    }

    fun decode(b: ByteArray): String {
        val hexChars = ByteArray(b.size * 2)
        for (j in b.indices) {
            val v: Int = (b[j] and 0xFF.toByte()).toInt()
            hexChars[j * 2] = HEX_CHAR_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v and 0x0F]
        }
        return String(hexChars, StandardCharsets.ISO_8859_1)
    }

    fun decode(b: Byte): String {
        val hexChars = ByteArray(2)
        val v: Int = (b and 0xFF.toByte()).toInt()
        hexChars[0] = HEX_CHAR_ARRAY[v ushr 4]
        hexChars[1] = HEX_CHAR_ARRAY[v and 0x0F]
        return String(hexChars, StandardCharsets.ISO_8859_1)
    }

    fun Bytes(s: String?): ByteArray? {
        return if (s == null) {
            null
        } else {
            encode(s)
        }
    }

    fun Int(s: String?): Int? {
        return if (s == null) {
            null
        } else {
            Int(Bytes(s))
        }
    }

    fun Complement(s: String?): String? {
        return StringChar(complement(Bytes(s)))
    }
}