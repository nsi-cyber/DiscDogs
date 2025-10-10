package com.discdogs.app.core.managers

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface SnackbarEvents {

    data class ErrorSnackbar(
        val errorMessage: String? = null,
    ) : SnackbarEvents

    data class SuccessSnackbar(
        val successMessage: String? = null,
    ) : SnackbarEvents


}

object SnackbarHostManager {
    private val _events = Channel<SnackbarEvents>()
    val events = _events.receiveAsFlow()

    suspend fun showError(message: String) {
        _events.send(
            SnackbarEvents.ErrorSnackbar(
                errorMessage = message,
            )
        )
    }

    suspend fun showSuccess(message: String) {
        _events.send(
            SnackbarEvents.SuccessSnackbar(
                successMessage = message
            )
        )
    }


}