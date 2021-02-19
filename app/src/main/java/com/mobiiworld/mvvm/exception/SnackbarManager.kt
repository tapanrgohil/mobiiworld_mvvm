package com.mobiiworld.mvvm.exception

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mobiiworld.mvvm.MyApp
import com.mobiiworld.mvvm.R
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Response

/**
 * This singleton class will be used to handle the snackbar throughout the app
 */
object SnackbarManager : ErrorCallback {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface SnackbarManagerPoint {
        fun getErrorResolver(): ErrorResolver
    }

    private lateinit var errorResolver: ErrorResolver

    init {
        val entryPoint = EntryPointAccessors.fromApplication(
            MyApp.INSTANCE, SnackbarManagerPoint::class.java
        )
        errorResolver = entryPoint.getErrorResolver()
    }

    private var snackbar: Snackbar? = null
    private var snackAnchorView: View? = null

    /**
     * Handles showing snackbar for errors.
     */
    fun AppCompatActivity.handleErrorResponse(
        retrofitResponse: Response<*>? = null,
        throwable: Throwable?,
        message: String? = null,
        retryAction: () -> Unit = {}
    ) {
        snackAnchorView = window.decorView.findViewById(android.R.id.content)
        //handle the responses
        errorResolver.handleErrorResponse(
            retrofitResponse,
            throwable,
            message,
            retryAction,
            this@SnackbarManager
        )
    }

    /**
     * Handles showing snackbar for "success" situations.
     */
    fun AppCompatActivity.handleSuccessResponse(message: String) {
        //show success snackbar with a message if view is not null else show a toast
        snackAnchorView = window.decorView.findViewById(android.R.id.content)
        if (snackAnchorView != null) {
            makeMessageSnackbar(message, false)
        } else {
            longToast(message)
        }
    }

    /**
     * Handles showing snackbar for errors.
     */
    fun Fragment.handleErrorResponse(
        retrofitResponse: Response<*>? = null,
        throwable: Throwable?,
        message: String? = null,
        retryAction: () -> Unit = {}
    ) {
        snackAnchorView = view
        //handle the responses
        errorResolver.handleErrorResponse(
            retrofitResponse,
            throwable,
            message,
            retryAction,
            this@SnackbarManager
        )
    }

    /**
     * Handles showing snackbar for "success" situations.
     */
    fun Fragment.handleSuccessResponse(message: String) {
        //show success snackbar with a message if view is not null else show a toast
        val view = view
        if (view != null) {
            makeMessageSnackbar(message, false)
        } else {
            longToast(message)
        }
    }

    /**
     * This will make snackbar with dismiss action.
     * Also, it will handle the colors for error and success.
     */
    private fun makeMessageSnackbar(message: String, isError: Boolean) {
        val view = snackAnchorView
        if (view != null) {
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar?.setAction(getString(R.string.dismiss)) {
                snackbar?.dismiss()
            }
            showSnackbar(message, isError)
        } else {
            longToast(message)
        }
    }

    /**
     * This is a helper function to show the snackbar
     */
    private fun showSnackbar(
        message: String = "",
        isError: Boolean = false
    ) {
        if (snackbar != null) {
            //https://stackoverflow.com/questions/30705607/android-multiline-snackbar
            snackbar?.view?.let { snackbarView ->
                val textView =
                    snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView?.maxLines = 5
            }

            //update colors and text for error snackbars
            if (isError) {
                val context = MyApp.INSTANCE
                snackbar?.setBackgroundTint(ContextCompat.getColor(context, R.color.snack_error))
                snackbar?.setActionTextColor(ContextCompat.getColor(context, R.color.white))
            }
            snackbar?.show()
        } else {
            //show toast
            longToast(message)
        }
    }

    /**
     * Dismiss the snackbar if back pressed in the activity
     */
    fun dismissSnackbar() {
        snackbar?.dismiss()
    }

    override fun onNoInternet(retryAction: () -> Unit) {
        val view = snackAnchorView
        val message = getString(R.string.no_internet_connectivity)
        if (view != null) {
            //this will make a snackbar with retry action
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            snackbar?.setAction(getString(R.string.retry)) {
                snackbar?.dismiss()
                retryAction.invoke()
            }
            showSnackbar(message, true)
        } else {
            longToast(message)
        }
    }

    override fun onUnknownError() {
        makeMessageSnackbar(getString(R.string.unknown_error), true)
    }

    override fun showError(errorMessage: String) {
        makeMessageSnackbar(errorMessage, true)
    }

    private fun longToast(message: String) {
        val context = MyApp.INSTANCE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun getString(@StringRes id: Int): String {
        val context = MyApp.INSTANCE
        return context.getString(id)
    }

}
