package com.tezov.bluetooth.external_lib_kotlin.util

object UtilsNull {
    val NULL_OBJECT = NULL()
    val NOT_NULL_OBJECT = NOT_NULL()

    class NULL {
        override fun equals(other: Any?): Boolean {
            return other is NULL
        }
        override fun hashCode(): Int {
            return 0
        }
        override fun toString(): String {
            return "NULL"
        }
    }

    class NOT_NULL {
        override fun equals(other: Any?): Boolean {
            return other is NOT_NULL
        }
        override fun hashCode(): Int {
            return 1
        }
        override fun toString(): String {
            return "NOT NULL"
        }
    }
}