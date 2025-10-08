package com.discdogs.app.core.managers

import com.discdogs.app.core.presentation.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface DialogEvents {
    data class NegativePositive(
        val title: UiText? = null,
        val desc: UiText,
        val positiveText: UiText,
        val negativeText: UiText,
        val onPositive: (() -> Unit)? = null,
        val onNegative: (() -> Unit)? = null,
    ) : DialogEvents
}

object DialogHostManager  {

    private val _events = Channel<DialogEvents>()
    val events = _events.receiveAsFlow()

    suspend fun showDialog(
        title: UiText? = null,
        desc: UiText,
        positiveText: UiText,
        negativeText: UiText,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null,
    ) {
        _events.send(
            DialogEvents.NegativePositive(
                title = title,
                desc = desc,
                positiveText = positiveText,
                negativeText =negativeText,
                onPositive = onPositive,
                onNegative = onNegative
            )
        )
    }

}
