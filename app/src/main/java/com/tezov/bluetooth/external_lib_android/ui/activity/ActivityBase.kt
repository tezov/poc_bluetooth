package com.tezov.bluetooth.external_lib_android.ui.activity

import androidx.activity.result.ActivityResultLauncher
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.tezov.bluetooth.external_lib_kotlin.type.LazyMutable
import com.tezov.bluetooth.external_lib_kotlin.type.collection.ListEntry
import com.tezov.bluetooth.external_lib_kotlin.util.ExtensionCompletable.notifyComplete
import com.tezov.bluetooth.external_lib_kotlin.util.ExtensionCompletable.onComplete
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates

abstract class ActivityBase protected constructor() : androidx.activity.ComponentActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val requestForResultStack: Queue<CompletableDeferred<ActivityResult>> by LazyMutable{
        LinkedList()
    }

    private lateinit var activityPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var requestForPermissionStack: Queue<CompletableDeferred<Map<String, Boolean>>> by LazyMutable{
        LinkedList()
    }

    @Composable
    abstract fun Content()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Content() }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                requestForResultStack.poll()?.notifyComplete(result)
            }
        activityPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                requestForPermissionStack.poll()?.notifyComplete(results)
            }
    }

    private fun launchForResult(task: CompletableDeferred<ActivityResult>, intent: Intent) {
        requestForResultStack.offer(task)
        activityResultLauncher.launch(intent)
    }

    private fun launchForResultCancelled(task: CompletableDeferred<ActivityResult>) {
        requestForResultStack.remove(task)
    }

    private fun launchForPermission(
        task: CompletableDeferred<Map<String, Boolean>>,
        permissions: Array<String>
    ) {
        requestForPermissionStack.offer(task)
        activityPermissionLauncher.launch(permissions)
    }

    private fun launchForPermissionCancelled(task: CompletableDeferred<Map<String, Boolean>>) {
        requestForPermissionStack.remove(task)
    }

    class RequestForResult(private val activity: ActivityBase, val intent: Intent) {
        data class Response(val resultCode: Int, val intent: Intent?) {
            fun isOk() = resultCode == RESULT_OK
            fun isNotOk() = resultCode != RESULT_OK
        }

        suspend fun response(): Response {
            var response by Delegates.notNull<Response>()
            suspendCancellableCoroutine { continuation ->
                val task: CompletableDeferred<ActivityResult> = CompletableDeferred()
                task.onComplete {
                    response = Response(it.resultCode, it.data)
                    continuation.resumeWith(Result.success(Unit))
                }
                activity.launchForResult(task, intent)
                continuation.invokeOnCancellation {
                    activity.launchForResultCancelled(task)
                }
            }
            return response
        }
    }

    class RequestForPermission(private val activity: ActivityBase) {
        private val requestedPermissions: MutableList<String> = ArrayList()
        private val resultPermissions: ListEntry<String, Boolean> = ListEntry()

        fun add(permission: String) {
            requestedPermissions.add(permission)
        }

        fun add(permission: List<String>) {
            permission.forEach { add(it) }
        }

        class Response(private val resultPermissions: ListEntry<String, Boolean>) {
            fun isGranted(permission: String) = resultPermissions.getValue(permission) ?: false
            fun isNotGranted(permission: String) = !isGranted(permission)
            fun isAllGranted() = resultPermissions.find { !it.value }?.let { false } ?: true
            fun isNotAllGranted() = resultPermissions.find { !it.value }?.let { true } ?: false

            fun denied(): List<String> = resultPermissions.filter { !it.value }.map { it.key }
            fun granted(): List<String> = resultPermissions.filter { it.value }.map { it.key }
        }

        suspend fun response(): Response {
            if (!requestedPermissions.isEmpty()) {
                suspendCancellableCoroutine { continuation ->
                    val task: CompletableDeferred<Map<String, Boolean>> = CompletableDeferred()
                    task.onComplete {
                        for ((key, value) in it) {
                            resultPermissions.put(key, value)
                        }
                        continuation.resumeWith(Result.success(Unit))
                    }
                    activity.launchForPermission(task, requestedPermissions.toTypedArray())
                    continuation.invokeOnCancellation {
                        activity.launchForPermissionCancelled(task)
                    }
                }
            }
            return Response(resultPermissions)
        }
    }
}