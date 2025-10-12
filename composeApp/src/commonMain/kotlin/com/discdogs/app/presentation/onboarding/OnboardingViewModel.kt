package com.discdogs.app.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OnboardingViewModel(
    private val libraryRepository: LibraryRepository
) : BaseViewModel<OnboardingState, OnboardingEffect, OnboardingEvent, OnboardingNavigator>() {

    private val _state = MutableStateFlow(OnboardingState())
    override val state: StateFlow<OnboardingState>
        get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    override val effect: SharedFlow<OnboardingEffect>
        get() = _effect


    override fun process(event: OnboardingEvent) {
        when (event) {


            is OnboardingEvent.OnPageChange -> {
                if (_state.value.pages.size > event.index) {
                    viewModelScope.launch {
                        _state.update { it.copy(currentPageIndex = event.index) }
                        _effect.emit(OnboardingEffect.OnPageChanged(event.index))
                    }
                } else {
                    viewModelScope.launch {
                        // Mark that user has completed onboarding
                        libraryRepository.setFirstTimeCompleted()
                        navigator?.navigateToMainScreen()
                    }
                }
            }


        }
    }


}
