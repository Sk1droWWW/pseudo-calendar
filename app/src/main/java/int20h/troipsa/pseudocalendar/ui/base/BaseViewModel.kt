package int20h.troipsa.pseudocalendar.ui.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import int20h.troipsa.pseudocalendar.ui.base.errors.AppError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : ViewModel() {

    private val coroutineExecutor = CoroutineExecutor(dispatcher)

    val scope: CoroutineScope
        get() = coroutineExecutor.scope

    /** Flow for posting error to UI*/
    val error: SharedFlow<AppError>
        get() = coroutineExecutor.error

    /** Flow for showing progress on UI*/
    val progress: StateFlow<Boolean>
        get() = coroutineExecutor.progress

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        coroutineExecutor.cancelScope()
    }

    fun handleException(throwable: Throwable) {
        coroutineExecutor.handleException(throwable)
    }

    protected fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        withProgress: Boolean = false,
        exceptionHandler: (suspend (Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return coroutineExecutor.runCoroutine(context, withProgress, exceptionHandler, block)
    }

    protected fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        progressHandler: (suspend (Boolean) -> Unit)?,
        exceptionHandler: (suspend (Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return coroutineExecutor.runCoroutine(context, progressHandler, exceptionHandler, block)
    }
}