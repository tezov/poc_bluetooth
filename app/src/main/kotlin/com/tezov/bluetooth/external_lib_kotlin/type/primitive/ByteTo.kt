package com.tezov.bluetooth.external_lib_kotlin.type.primitive

import com.tezov.bluetooth.external_lib_kotlin.type.primitive.string.StringHexTo

object ByteTo {
    var BYTES = 1
    var SIZE = java.lang.Byte.SIZE
    fun Boolean(b: Byte): Boolean {
        return b.toInt() == 1
    }

    fun StringHex(b: Byte): String {
        return StringHexTo.decode(b)
    }
}