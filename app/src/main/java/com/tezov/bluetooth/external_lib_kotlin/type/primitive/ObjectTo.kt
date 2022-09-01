package com.tezov.bluetooth.external_lib_kotlin.type.primitive

object ObjectTo {
    fun hashcodeIdentity(o: Any?): Int {
        return System.identityHashCode(o)
    }

    fun hashcodeIdentityString(o: Any?): String {
        return if (o == null) {
            "object is null"
        } else "0x" + Integer.toHexString(System.identityHashCode(o))
    }

    fun hashcodeString(o: Any?): String {
        return if (o == null) {
            "object is null"
        } else "0x" + Integer.toHexString(o.hashCode())
    }
}