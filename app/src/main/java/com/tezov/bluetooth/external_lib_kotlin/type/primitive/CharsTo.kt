package com.tezov.bluetooth.external_lib_kotlin.type.primitive

object CharsTo {
    fun Bytes(c: CharArray?): ByteArray? {
        if (c == null) {
            return null
        }
        val b = ByteArray(c.size * 2)
        var i = 0
        while (i < b.size) {
            val o = c[i / 2]
            b[i] = o.code.toByte()
            b[i + 1] = (o.code shr 8).toByte()
            i += 2
        }
        return b
    }
}