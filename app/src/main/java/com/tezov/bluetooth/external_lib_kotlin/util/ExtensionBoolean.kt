package com.tezov.bluetooth.external_lib_kotlin.util


object ExtensionBoolean {

    inline fun <T> Boolean.onTrue(run: () -> T): T?{
        if(this){
            return run()
        }
        return null
    }
    inline fun <T> Boolean.onFalse(run: () -> T): T?{
        if(!this){
            return run()
        }
        return null
    }
}