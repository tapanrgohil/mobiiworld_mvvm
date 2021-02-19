package com.mobiiworld.mvvm.util

import android.view.View
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.core.Status
import com.mobiiworld.mvvm.exception.SnackbarManager.handleErrorResponse


fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(boolean: Boolean) {
    if (boolean) {
        visible()
    } else {
        gone()
    }
}

fun View.visibleInVisible(boolean: Boolean) {
    if (boolean) {
        visible()
    } else {
        invisible()
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, androidx.lifecycle.Observer(body))


fun <R> Fragment.handleResponse(
    liveData: LiveData<Resource<R>>,
    progressView: View? = null,
    @UiThread
    swipeRefreshLayout: SwipeRefreshLayout? = null,
    process: (R) -> Unit
) {
    observe(liveData) {
        it?.apply {
            when (this.status) {
                Status.SUCCESS -> {
                    data?.let { it1 -> process.invoke(it1) }
                    swipeRefreshLayout?.isRefreshing = false
                    progressView?.gone()
                }
                Status.ERROR -> {
                    swipeRefreshLayout?.isRefreshing = false
                    throwable?.let { exception ->
                        handleErrorResponse(it.retrofitResponse, it.throwable)
                    }
                    progressView?.gone()
                }
                Status.LOADING -> {
                    swipeRefreshLayout?.isRefreshing = true
                    progressView?.visible()
                }
            }
        }
    }
}


fun <R> LifecycleOwner.handleResponse(
    liveData: LiveData<Resource<R>>,
    progressView: View? = null,
    @UiThread
    process: (R) -> Unit,
) {
    observe(liveData) {
        it?.apply {
            when (this.status) {
                Status.SUCCESS -> {
                    data?.let { it1 -> process.invoke(it1) }
                    if (progressView == null)
//                        handleLoadingDialog(LoadingDialog.LoadingStates.Done)
                        progressView?.gone()
                }
                Status.ERROR -> {
//                    myException?.let { exception ->
//                        onError(exception)
//                    }
                    progressView?.gone()
                }
                Status.LOADING -> {
                    if (progressView == null)
//                        handleLoadingDialog(LoadingDialog.LoadingStates.Loading)
                        progressView?.visible()
                }
            }
        }
    }
}


