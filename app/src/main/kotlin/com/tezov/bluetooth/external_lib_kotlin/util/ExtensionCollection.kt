package com.tezov.bluetooth.external_lib_kotlin.util

object ExtensionCollection {

    inline fun <T> Iterable<T>.forEach(offset:Int = 0, action: (T) -> Unit) {
        drop(offset).forEach(action)
    }
    inline fun <T> Iterable<T>.forEachIndexed(offset:Int = 0, action: (index: Int, T) -> Unit) {
        var index = offset
        drop(offset).forEach {
            item -> action(index, item)
            index++
        }
    }

    inline fun <T> Iterable<T>.find(offset:Int = 0, predicate: (T) -> Boolean): T? = drop(offset).find(predicate)
    inline fun <T> Iterable<T>.findIndexed(offset:Int = 0, predicate: (index: Int, T) -> Boolean): T? {
        forEachIndexed(offset){ index, element ->
            if (predicate(index, element)) return element
        }
        return null
    }

    inline fun <T> Iterable<T>.findIndex(offset:Int = 0, predicate: (T) -> Boolean): Int? {
        forEachIndexed(offset){ index, element ->
            if (predicate(element)) return index
        }
        return null
    }
}