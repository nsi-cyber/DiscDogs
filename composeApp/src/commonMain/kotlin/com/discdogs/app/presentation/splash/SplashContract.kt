package com.discdogs.app.presentation.splash

data class SplashState(
    val isLoading: Boolean = false,
)

sealed interface SplashEffect {
    data class ShowToast(val message: String) : SplashEffect
}

sealed class SplashEvent {
    object OnBackClicked : SplashEvent()
}