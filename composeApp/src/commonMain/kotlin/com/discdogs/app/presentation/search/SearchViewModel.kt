package com.discdogs.app.presentation.search

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.data.repository.LibraryRepository
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.presentation.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val networkRepository: NetworkRepository,
    private val libraryRepository: LibraryRepository
) : BaseViewModel<SearchState, SearchEffect, SearchEvent, SearchNavigator>() {

    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    override val effect: SharedFlow<SearchEffect> get() = _effect


    private var searchJob: Job? = null

    init {
        loadRecentItems()
    }

    private fun loadRecentItems() {
        viewModelScope.launch {
            libraryRepository.getRecentReleasesBySource("SEARCH")
                .distinctUntilChanged()
                .collect { recentSearched ->
                    _state.update { it.copy(recentSearchedReleases = recentSearched) }
                }
        }

        viewModelScope.launch {
            libraryRepository.getRecentReleasesBySource("SCAN")
                .distinctUntilChanged()
                .collect { recentScanned ->
                    _state.update { it.copy(recentScannedReleases = recentScanned) }
                }
        }
    }

    override fun process(event: SearchEvent) {
        when (event) {
            SearchEvent.OnBackClicked -> navigator?.navigateBack()
            is SearchEvent.OnSearchQuery -> {
                _state.update { it.copy(searchQuery = event.query, isLoading = true) }
                if (event.query.isNotEmpty())
                    searchWithDebounce(event.query)
                else {
                    searchJob?.cancel()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            resultList = null,
                            searchQuery = "",
                        )
                    }

                }
            }

            SearchEvent.OnClearQuery -> {
                searchJob?.cancel()
                _state.update {
                    it.copy(
                        resultList = null,
                        searchQuery = "",
                    )
                }

            }

            is SearchEvent.OnReleaseDetail -> {
                navigator?.navigateToReleaseDetail(event.data.id, event.data.thumb)
            }

            is SearchEvent.OnRecentSearchedReleaseClick -> {
                navigator?.navigateToReleaseDetail(event.release.id, event.release.thumb)
            }

            is SearchEvent.OnRecentScannedReleaseClick -> {
                navigator?.navigateToReleaseDetail(event.release.id, event.release.thumb)
            }
        }
    }

    private fun searchWithDebounce(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            getSearch(query)
        }
    }

    private fun getSearch(query: String) {
        viewModelScope.launch {
            when (val result = networkRepository.searchVinyl(query)) {
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
