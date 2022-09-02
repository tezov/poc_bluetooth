package com.tezov.bluetooth.external_lib_kotlin.type.primitive.string

import android.util.Base64
import com.tezov.bluetooth.external_lib_kotlin.type.primitive.BytesTo

object StringBase64To {
    fun encode(b: ByteArray) = Base64.encodeToString(b, Base64.DEFAULT)
    fun decode(s: String) = Base64.decode(s, Base64.DEFAULT)

    fun Bytes(s: String) = decode(s)
    fun StringChar(s: String) = BytesTo.StringChar(decode(s))
}