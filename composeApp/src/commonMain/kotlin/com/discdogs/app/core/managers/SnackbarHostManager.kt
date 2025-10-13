package com.discdogs.app.core.managers

import com.discdogs.app.core.presentation.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface SnackbarEvents {

    data class ErrorSnackbar(
        val errorMessage: UiText? = null,
    ) : SnackbarEvents

    data class SuccessSnackbar(
        val successMessage: UiText? = null,
    ) : SnackbarEvents


}

object SnackbarHostManager {
    private val _events = Channel<SnackbarEvents>()
    val events = _events.receiveAsFlow()

    suspend fun showError(message: UiText) {
        _events.send(
            SnackbarEvents.ErrorSnackbar(
                errorMessage = message,
            )
        )
    }

    suspend fun showSuccess(message: UiText) {
        _events.send(
            SnackbarEvents.SuccessSnackbar(
                successMessage = message
            )
        )
    }


}