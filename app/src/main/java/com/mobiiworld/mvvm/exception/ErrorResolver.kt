package com.mobiiworld.mvvm.exception

import android.app.Application
import androidx.hilt.Assisted
import com.mobiiworld.mvvm.R
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Error handling for API responses. [handleErrorResponse] is the entry point for this.
 * [ErrorCallback] can be implemented at the view level to show appropriate UI for the error.
 */
@Singleton
class ErrorResolver @Inject constructor(@Assisted private val context: Application) {

    fun handleErrorResponse(
        retrofitResponse: Response<*>? = null,
        throwable: Throwable?,
        message: String? = null,
        retryAction: () -> Unit = {},
        errorCallback: ErrorCallback
    ) {
        //handle the responses
        when {
            throwable.isNetworkError() -> errorCallback.onNoInternet(retryAction)
            else -> handleNonNetworkErrorResponse(
                retrofitResponse,
                throwable,
                message,
                errorCallback
            )
        }
    }

    private fun handleNonNetworkErrorResponse(
        response: Response<*>?,
        throwable: Throwable?,
        message: String?,
        errorCallback: ErrorCallback
    ) {
        //this is api error show error snackbar
        val errorMessage = when (response?.code()) {
            HTTP_409_CONFLICT_RESPONSE, HTTP_422_UNPROCESSIBLE_ENTITY -> {
                //todo parse custom error message from api response
                ""
            }
            HTTP_404_NOT_FOUND -> {
                context.getString(R.string.link_not_found)
            }
            HTTP_403_FORBIDDEN -> context.getString(R.string.access_denied)
            HTTP_401_UNAUTHORIZED -> {
                context.run {
                    //redirect user to login
                }
                ""
            }
            HTTP_500_INTERNAL_ERROR -> context.getString(R.string.server_error)
            else -> {
                if (throwable == null) {
                    if (message != null) {
                        message
                    } else {
                        //we shouldn't get to this state!
                        //Trying to handle an error with a null throwable!
                        errorCallback.onUnknownError()
                        ""
                    }
                } else {
                    //we have a non-HTTP, non-network exception like CancellationException,
                    // TooManyRequestsException, AppException, etc
                    if (throwable is AppException) {
                        //we've already figured out what message should be displayed here
                        throwable.message
                    } else {
                        //let AppException figure out what should be the message we display
                        AppException(throwable).message
                    }
                }
            }
        }

        if (errorMessage.isNotBlank()) {
            errorCallback.showError(errorMessage)
        }
    }
}

interface ErrorCallback {
    fun onNoInternet(retryAction: () -> Unit)

    fun onUnknownError()

    fun showError(errorMessage: String)
}
