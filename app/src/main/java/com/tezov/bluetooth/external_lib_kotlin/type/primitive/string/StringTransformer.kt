package com.tezov.bluetooth.external_lib_kotlin.type.primitive.string

abstract class StringTransformer {
    abstract fun alter(s: String?): String?
    abstract fun restore(s: String?): String?
}