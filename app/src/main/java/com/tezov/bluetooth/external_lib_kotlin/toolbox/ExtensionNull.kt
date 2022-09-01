package com.tezov.bluetooth.external_lib_kotlin.toolbox

import java.util.*

object ExtensionNull {

    fun String?.isNullOrEmpty() = this?.let { length <= 0 } ?: let { true }
    fun String?.isNotNullAndNotEmpty() = this?.let { length > 0 } ?: let { false }

    fun CharSequence?.isNullOrEmpty() = this?.let { length <= 0 } ?: let { true }
    fun CharSequence?.isNotNullAndNotEmpty() = this?.let { length > 0 } ?: let { false }

    fun ByteArray?.nullify() = this?.let {
        val byte = 0.toByte()
        Arrays.fill(this, byte)
    }
    fun ByteArray?.isNullOrEmpty() = this?.let { size <= 0 } ?: let { true }
    fun ByteArray?.isNotNullAndNotEmpty() = this?.let { size > 0 } ?: let { false }

    fun CharArray?.nullify() = this?.let {
        val char = 0.toChar()
        Arrays.fill(this, char)
    }
    fun CharArray?.isNullOrEmpty() = this?.let { size <= 0 } ?: let { true }
    fun CharArray?.isNotNullAndNotEmpty() = this?.let { size > 0 } ?: let { false }




}
