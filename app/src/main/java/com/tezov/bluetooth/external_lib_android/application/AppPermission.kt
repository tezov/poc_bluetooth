package com.tezov.bluetooth.external_lib_android.application

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.tezov.bluetooth.external_lib_android.ui.activity.ActivityBase
import com.tezov.bluetooth.external_lib_android.ui.activity.ActivityBase.RequestForPermission
import com.tezov.bluetooth.external_lib_kotlin.type.collection.ListEntry

object AppPermission {
    fun isGranted(context: Context, permission: String) = ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    class Checker(internal val activity: ActivityBase) {
        private val resultPermissions = ListEntry<String, Boolean>()
        private lateinit var requestForPermission:RequestForPermission

        fun add(permission: String) {
            resultPermissions.add(permission, isGranted(activity.applicationContext, permission))
        }
        fun add(permission: List<String>) {
            permission.forEach { add(it) }
        }

        fun check() = RequestForPermission.Response(resultPermissions)
        suspend fun response(): RequestForPermission.Response{
            val response = check()
            return if(response.isAllGranted()){
                response
            } else{
                requestForPermission = RequestForPermission(activity).apply {
                    add(response.denied())
                }
                requestForPermission.response()
            }
        }

    }
}