package int20h.troipsa.pseudocalendar.utils.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * Collect data from Coroutine Flow.
 *
 * @param T type of the flow data
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 */
suspend inline fun <reified T> Flow<T>.collect(
    onlyOnce: Boolean = false,
    crossinline action: suspend (value: T) -> Unit,
) {
    var flow = this

    if (onlyOnce) {
        flow = flow.take(1)
    }

    flow.collect { value ->
        coroutineContext.ensureActive()
        action(value)
    }
}

/**
 * Collect not nullable data from Coroutine Flow.
 *
 * @param T type of the flow data
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 */
suspend inline fun <reified T : Any> Flow<T?>.collectNotNull(
    onlyOnce: Boolean = false,
    crossinline action: suspend (value: T) -> Unit,
) {
    return filterNotNull().collect(onlyOnce, action)
}

/**
 * Collect data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T> Flow<T>.launchAndCollect(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend (value: T) -> Unit,
): Job {
    return scope.launch(context) {
        collect(onlyOnce, action)
    }
}

/**
 * Collect not nullable data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T : Any> Flow<T?>.launchAndCollectNotNull(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend (value: T) -> Unit,
): Job {
    return filterNotNull().launchAndCollect(scope, context, onlyOnce, action)
}

/**
 * Returns a flow containing the results of applying the given [transform] function to each value of the original flow.
 */
inline fun <T, R> Flow<T?>.mapOrNull(crossinline transform: suspend (value: T) -> R): Flow<R?> {
    return transform { value -> emit(value?.let { transform(it) }) }
}

/**
 * Collect data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
@Deprecated(
    "Use extension with more convenient name \"launchAndCollect\", or suspend \"collect\"",
    replaceWith = ReplaceWith("this.launchAndCollect(scope, onlyOnce=onlyOnce, action)"),
)
inline fun <reified T> Flow<T>.collect(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit,
): Job {
    return scope.launch(context) {
        collect {
            ensureActive()
            action.invoke(this, it)
            if (onlyOnce) {
                cancel()
            }
        }
    }
}

/**
 * Collect not nullable data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
@Deprecated(
    "Use extension with more convenient name \"launchAndCollectNotNull\", or suspend \"collect\"",
    replaceWith = ReplaceWith("this.launchAndCollectNotNull(scope, onlyOnce=onlyOnce, action)"),
)
inline fun <reified T : Any> Flow<T?>.collectNotNull(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit,
): Job {
    return filterNotNull().collect(scope, context, onlyOnce, action)
}