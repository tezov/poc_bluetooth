package com.tezov.bluetooth.external_lib_kotlin.type.primitive.string

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.LongTo
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo
import java.nio.charset.StandardCharsets

object StringCharTo {
    private const val PRIME = 1125899906842597L
    fun encode(s: String) = s.toByteArray(StandardCharsets.UTF_8)

    fun decode(bytes: ByteArray) = String(bytes, StandardCharsets.UTF_8)

    fun LongHashcode64(string: String): Long {
        var h = PRIME
        val len = string.length
        for (i in 0 until len) {
            h = 31 * h + string[i].code.toLong()
        }
        return h
    }

    fun BytesHashcode64(string: String) = LongTo.Bytes(LongHashcode64(string))

    fun StringHexHashcode64(string: String) = LongTo.StringHex(LongHashcode64(string))

    fun LongHashcode8(string: String) = LongHashcode64(string).toByte()

    fun Bytes(s: String) = encode(s)

    fun StringHex(s: String) = BytesTo.StringHex(encode(s))

    fun StringBase64(s: String) = BytesTo.StringBase64(encode(s))

    fun StringBase58(s: String) = BytesTo.StringBase58(encode(s))

    fun StringBase49(s: String) = BytesTo.StringBase49(encode(s))

    fun Complement(s: String) = BytesTo.StringHex(BytesTo.complement(Bytes(s)))
}