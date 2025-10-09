package com.discdogs.app.presentation.releases

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.discdogs.app.app.Route
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReleasesViewModel(
    private val networkRepository: NetworkRepository,
    private val savedStateHandle: SavedStateHandle

) : BaseViewModel<ReleasesState, ReleasesEffect, ReleasesEvent, ReleasesNavigator>() {

    private val _state = MutableStateFlow(ReleasesState())
    override val state: StateFlow<ReleasesState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReleasesEffect>()
    override val effect: SharedFlow<ReleasesEffect> get() = _effect

    init {
        getMastersVersions(savedStateHandle.toRoute<Route.ReleaseVersions>().masterId)
    }

    override fun process(event: ReleasesEvent) {
        when (event) {
            ReleasesEvent.OnBackClicked -> navigator?.navigateBack()

            is ReleasesEvent.OnReleaseDetail -> {
                navigator?.navigateToReleaseDetail(event.data.id, event.data.thumb)
            }
        }
    }

    private fun getMastersVersions(masterId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = networkRepository.getMastersVersions(masterId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            resultList = result.value?.map { it.toUiModel() },
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
