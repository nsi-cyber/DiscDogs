package com.discdogs.app.presentation.splash

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val libraryRepository: LibraryRepository,
) : BaseViewModel<SplashState, SplashEffect, SplashEvent, SplashNavigator>() {

    private val _state = MutableStateFlow(SplashState())
    override val state: StateFlow<SplashState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>()
    override val effect: SharedFlow<SplashEffect> get() = _effect


    override fun process(event: SplashEvent) {
        when (event) {
            SplashEvent.OnBackClicked -> navigator?.navigateBack()
        }
    }

    init {

        viewModelScope.launch {
            val isFirstTime = libraryRepository.isFirstTime()
            if (isFirstTime) {
                navigator?.navigateToOnboardingScreen()
            } else {
                navigator?.navigateToMainScreen()
            }
        }

    }
}
