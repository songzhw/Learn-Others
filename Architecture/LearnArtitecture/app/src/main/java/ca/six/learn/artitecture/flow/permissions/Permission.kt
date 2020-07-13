package ca.six.learn.artitecture.flow.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


data class Permission(
    val permission: String,
    val isGranted: Boolean,
    val shouldShowRational: Boolean = false
)


class PermissionFragment : Fragment() {
    val REQUEST_CODE_PERMISSION = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun request(vararg permissions: String) {
        requestPermissions(permissions, REQUEST_CODE_PERMISSION)
    }

    var completableDeferred: CompletableDeferred<List<Permission>> = CompletableDeferred()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_CODE_PERMISSION || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        completableDeferred.complete(permissions.map {
            Permission(
                it,
                isPermissionGranted(it),
                showRequestPermissionRationale(it)
            )
        })
        completableDeferred = CompletableDeferred()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showRequestPermissionRationale(permission: String) =
        activity?.run {
            !isPermissionGranted(permission) && shouldShowRequestPermissionRationale(permission)
        } ?: false

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isPermissionGranted(permission: String): Boolean =
        activity?.run { checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED }
            ?: false

    override fun onDestroy() {
        super.onDestroy()
        if (completableDeferred.isActive) {
            completableDeferred.cancel()
        }
        completableDeferred = CompletableDeferred()
    }

}


object PermissionFlow {

    internal var permissionsToRequest: Array<out String> = emptyArray()
    internal var activityInFlow: FragmentActivity? = null

    private var permissionFragment: PermissionFragment? = null

    fun request() = flow {
        createFragment()

        permissionFragment?.takeIf { permissionsToRequest.isNotEmpty() }?.run {
            request(*permissionsToRequest)
            val results = completableDeferred.await()
            if (results.isNotEmpty()) {
                emit(results.all { it.isGranted })
            }
        }
    }

    private fun createFragment() = permissionFragment?.let {
        addFragment(it)
    } ?: PermissionFragment().let {
        permissionFragment = it
        addFragment(it)
    }

    private fun addFragment(fragment: PermissionFragment) {
        val fragmentManager = activityInFlow?.supportFragmentManager
            ?: throw IllegalArgumentException("To work properly you need to pass an instance of a Fragment or a FragmentActivity")

        fragmentManager
            .beginTransaction()
            .apply {
                if (fragment.isAdded) {
                    detach(fragment)
                }
            }
            .add(fragment, PermissionFragment::class.java.simpleName)
            .commitNow()
    }

}

@RequiresApi(Build.VERSION_CODES.M)
suspend fun permissionFlow(fn: suspend PermissionFlow.() -> Unit) {
    fn(PermissionFlow)
}

fun PermissionFlow.withPermissions(vararg permissions: String) {
    permissionsToRequest = permissions
}

fun PermissionFlow.withActivity(activity: FragmentActivity) {
    activityInFlow = activity
}

class PermissionFlowDemo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            permissionFlow {
                withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                withActivity(this@PermissionFlowDemo)
                request().collect { granded: Boolean -> println("okay") }
            }
        }

    }
}