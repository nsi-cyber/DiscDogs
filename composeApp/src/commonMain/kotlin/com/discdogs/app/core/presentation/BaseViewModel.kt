package com.discdogs.app.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.managers.DialogHostManager
import com.discdogs.app.core.managers.SnackbarHostManager
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel<STATE, EFFECT, EVENT, NAVIGATOR> : ViewModel() {

    var navigator: NAVIGATOR? = null
        private set
    abstract val state: StateFlow<STATE>
    abstract val effect: SharedFlow<EFFECT>
    abstract fun process(event: EVENT)

    fun setNavigator(navigator: NAVIGATOR) {
        this.navigator = navigator
    }


    fun errorSnack(message: UiText) {
        viewModelScope.launch {
            SnackbarHostManager.showError(message)
        }
    }

    fun showDialog(
        title: UiText? = null,
        desc: UiText,
        positiveText: UiText,
        negativeText: UiText,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            DialogHostManager.showDialog(
                title = title,
                desc = desc,
                positiveText = positiveText,
                negativeText = negativeText,
                onPositive = onPositive,
                onNegative = onNegative
            )
        }
    }
}