package com.tezov.bluetooth.external_lib_kotlin.type.primitive.string

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringBase49To
import java.nio.charset.StandardCharsets
import java.util.*

object StringBase49To {
    val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()
    private val INDEXES = IntArray(128)
    @JvmStatic
    fun encode(input: ByteArray): String {
        var input = input
        if (input.size == 0) {
            return ""
        }
        input = copyOfRange(input, 0, input.size)
        var zeroCount = 0
        while (zeroCount < input.size && input[zeroCount] == 0.toByte()) {
            ++zeroCount
        }
        val temp = ByteArray(input.size * 2)
        var j = temp.size
        var startAt = zeroCount
        while (startAt < input.size) {
            val mod = divmod49(input, startAt)
            if (input[startAt] == 0.toByte()) {
                ++startAt
            }
            temp[--j] = ALPHABET[mod.toInt()].code.toByte()
        }
        while (j < temp.size && temp[j] == ALPHABET[0].code.toByte()) {
            ++j
        }
        while (--zeroCount >= 0) {
            temp[--j] = ALPHABET[0].toByte()
        }
        val output = copyOfRange(temp, j, temp.size)
        return String(output, StandardCharsets.UTF_8)
    }

    fun decode(input: String): ByteArray? {
        val input58 = ByteArray(input.length)
        for (i in 0 until input.length) {
            val c = input[i]
            var digit58 = -1
            if (c.code < 128) {
                digit58 = INDEXES[c.code]
            }
            if (digit58 < 0) {
//            DebugException.start().log("Illegal character " + c + " at " + i).end();
                return null
            }
            input58[i] = digit58.toByte()
        }
        var zeroCount = 0
        while (zeroCount < input58.size && input58[zeroCount] == 0.toByte()) {
            ++zeroCount
        }
        val temp = ByteArray(input.length)
        var j = temp.size
        var startAt = zeroCount
        while (startAt < input58.size) {
            val mod = divmod256(input58, startAt)
            if (input58[startAt] == 0.toByte()) {
                ++startAt
            }
            temp[--j] = mod
        }
        while (j < temp.size && temp[j] == 0.toByte()) {
            ++j
        }
        return copyOfRange(temp, j - zeroCount, temp.size)
    }

    private fun divmod49(number: ByteArray, startAt: Int): Byte {
        var remainder = 0
        for (i in startAt until number.size) {
            val digit256 = number[i].toInt() and 0xFF
            val temp = remainder * 256 + digit256
            number[i] = (temp / 49).toByte()
            remainder = temp % 49
        }
        return remainder.toByte()
    }

    private fun divmod256(number49: ByteArray, startAt: Int): Byte {
        var remainder = 0
        for (i in startAt until number49.size) {
            val digit49 = number49[i].toInt() and 0xFF
            val temp = remainder * 49 + digit49
            number49[i] = (temp / 256).toByte()
            remainder = temp % 256
        }
        return remainder.toByte()
    }

    private fun copyOfRange(source: ByteArray, from: Int, to: Int): ByteArray {
        val range = ByteArray(to - from)
        System.arraycopy(source, from, range, 0, range.size)
        return range
    }

    fun Bytes(s: String) = decode(s)

    fun StringChar(s: String) = BytesTo.StringChar(decode(s))

    init {
        Arrays.fill(INDEXES, -1)
        for (i in ALPHABET.indices) {
            INDEXES[ALPHABET[i].code] = i
        }
    }
}