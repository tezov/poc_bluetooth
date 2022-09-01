package com.tezov.bluetooth.external_lib_kotlin.type

import kotlin.reflect.KProperty

//todo find how to make resettable

class LazyMutable<T>(val initializer: () -> T) {
    private object UninitializedValue
    @Volatile private var propValue: Any? = UninitializedValue
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val localValue = propValue
        if(localValue != UninitializedValue) {
            return localValue as T
        }
        return synchronized(this) {
            val localValue2 = propValue
            if (localValue2 != UninitializedValue) {
                localValue2 as T
            } else {
                val initializedValue = initializer()
                propValue = initializedValue
                initializedValue
            }
        }
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        synchronized(this) {
            propValue = value
        }
    }

}