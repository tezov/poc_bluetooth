package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.ByteTo

object CharTo {
    var BYTES = Character.SIZE / ByteTo.SIZE
    fun Bytes(c: Char): ByteArray {
        val b = ByteArray(2)
        b[0] = c.code.toByte()
        b[1] = (c.code shr 8).toByte()
        return b
    }
}