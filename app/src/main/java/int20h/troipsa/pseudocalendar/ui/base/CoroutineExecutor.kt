package int20h.troipsa.pseudocalendar.ui.base

import int20h.troipsa.pseudocalendar.ui.base.errors.AppError
import int20h.troipsa.pseudocalendar.ui.base.errors.NoNetworkError
import int20h.troipsa.pseudocalendar.ui.base.errors.OtherError
import int20h.troipsa.pseudocalendar.ui.base.errors.TimeoutError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CoroutineExecutor(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    val scope = CoroutineScope(dispatcher + SupervisorJob() + exceptionHandler)

    /**Flow for posting error to UI*/
    private val _error by lazy { MutableSharedFlow<AppError>() }
    val error by lazy { _error.asSharedFlow() }

    /**Flow for showing progress on UI*/
    private val _progress by lazy { MutableStateFlow(false) }
    val progress by lazy { _progress.asStateFlow() }

    fun cancelScope() {
        scope.coroutineContext.cancelChildren()
    }

    fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        withProgress: Boolean = false,
        exceptionHandler: (suspend (Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return scope.launch(context) {
            execute(withProgress, exceptionHandler, block)
        }
    }

    fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        progressHandler: (suspend (Boolean) -> Unit)?,
        exceptionHandler: (suspend (Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return scope.launch(context) {
            execute(progressHandler, exceptionHandler, block)
        }
    }

    suspend fun execute(
        withProgress: Boolean,
        exceptionHandler: (suspend (Exception) -> Boolean)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (withProgress) {
            execute(_progress::emit, exceptionHandler, block)
        } else {
            execute(null, exceptionHandler, block)
        }
    }

    suspend fun execute(
        progressHandler: (suspend (Boolean) -> Unit)?,
        exceptionHandler: (suspend (Exception) -> Boolean)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            progressHandler?.invoke(true)

            try {
                block.invoke(this)
            } catch (ex: Exception) {
                if (exceptionHandler?.invoke(ex) == true) {
                } else {
                    throw ex
                }
            } finally {
                progressHandler?.invoke(false)
            }
        }
    }

    fun handleException(throwable: Throwable) {
        scope.launch {
            val appError = when (throwable) {
                is SocketTimeoutException -> TimeoutError(throwable)
                is UnknownHostException -> NoNetworkError(throwable)
                else -> OtherError(throwable)
            }
            _error.emit(appError)
        }
    }

}