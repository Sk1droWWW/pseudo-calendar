package int20h.troipsa.pseudocalendar.ui.base.errors

sealed class AppError(val throwable: Throwable)

class TimeoutError(throwable: Throwable) : AppError(throwable)

class NoNetworkError(throwable: Throwable) : AppError(throwable)

class OtherError(throwable: Throwable) : AppError(throwable)