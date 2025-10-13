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
        val masterId = savedStateHandle.toRoute<Route.ReleaseVersions>().masterId
        _state.update { it.copy(masterId = masterId) }
        getMastersVersions(masterId)
    }

    override fun process(event: ReleasesEvent) {
        when (event) {
            ReleasesEvent.OnBackClicked -> navigator?.navigateBack()

            ReleasesEvent.LoadMore -> {
                if (_state.value.hasMore && !_state.value.isLoadingMore && _state.value.masterId != null) {
                    loadMoreResults()
                }
            }

            is ReleasesEvent.OnReleaseDetail -> {
                navigator?.navigateToReleaseDetail(
                    event.data.id,
                    event.data.thumb,
                    savedStateHandle.toRoute<Route.ReleaseVersions>().source
                )
            }
        }
    }

    private fun getMastersVersions(masterId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result =
                networkRepository.getMastersVersions(masterId, perPage = 20, page = 1)) {
                is Resource.Success -> {
                    val newResults = result.value?.versions?.map { it.toUiModel() } ?: emptyList()
                    val pagination = result.value?.pagination
                    _state.update {
                        it.copy(
                            resultList = newResults,
                            isLoading = false,
                            currentPage = pagination?.page ?: 1,
                            hasMore = (pagination?.page ?: 1) < (pagination?.pages ?: 1),
                            totalPages = pagination?.pages
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun loadMoreResults() {
        viewModelScope.launch {
            val masterId = _state.value.masterId ?: return@launch
            _state.update { it.copy(isLoadingMore = true) }
            val nextPage = _state.value.currentPage + 1
            when (val result = networkRepository.getMastersVersions(
                masterId = masterId,
                perPage = 20,
                page = nextPage
            )) {
                is Resource.Success -> {
                    val newResults = result.value?.versions?.map { it.toUiModel() } ?: emptyList()
                    val pagination = result.value?.pagination
                    val currentResults = _state.value.resultList ?: emptyList()

                    // Combine and remove duplicates by id
                    val combinedResults = (currentResults + newResults).distinctBy { it.id }

                    _state.update {
                        it.copy(
                            resultList = combinedResults,
                            isLoadingMore = false,
                            currentPage = pagination?.page ?: nextPage,
                            hasMore = (pagination?.page ?: nextPage) < (pagination?.pages ?: 1),
                            totalPages = pagination?.pages
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoadingMore = false) }
                }
            }
        }
    }
}
