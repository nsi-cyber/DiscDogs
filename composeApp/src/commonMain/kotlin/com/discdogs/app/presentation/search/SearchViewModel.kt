package com.discdogs.app.presentation.search

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.data.repository.LibraryRepository
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.domain.SearchType
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
                            currentPage = 1,
                            hasMore = true,
                            totalPages = null
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
                        currentPage = 1,
                        hasMore = true,
                        totalPages = null
                    )
                }

            }

            SearchEvent.LoadMore -> {
                if (_state.value.hasMore && !_state.value.isLoadingMore && _state.value.searchQuery.isNotEmpty()) {
                    loadMoreResults()
                }
            }

            is SearchEvent.OnItemClicked -> {
                when (_state.value.searchType) {
                    SearchType.MASTER -> navigator?.navigateToMasterDetail(
                        event.data.id,
                        event.data.thumb
                    )

                    SearchType.RELEASE -> navigator?.navigateToReleaseDetail(
                        event.data.id,
                        event.data.thumb
                    )

                }
            }

            is SearchEvent.OnRecentSearchedReleaseClick -> {
                navigator?.navigateToMasterDetail(event.release.id, event.release.thumb)
            }

            is SearchEvent.OnRecentScannedReleaseClick -> {
                navigator?.navigateToReleaseDetail(event.release.id, event.release.thumb)
            }

            is SearchEvent.OnSearchTypeChanged -> {
                _state.update { it.copy(searchType = event.type) }
                if (_state.value.searchQuery.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            currentPage = 1,
                            hasMore = true,
                            totalPages = null
                        )
                    }
                    searchWithDebounce(_state.value.searchQuery)
                }
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
            when (val result =
                networkRepository.searchVinyl(
                    query,
                    type = _state.value.searchType,
                    perPage = 20,
                    page = 1
                )) {
                is Resource.Success -> {
                    val newResults =
                        result.value?.results?.map { it.toUiModel(isMaster = _state.value.searchType == SearchType.MASTER) }
                            ?: emptyList()
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
            _state.update { it.copy(isLoadingMore = true) }
            val nextPage = _state.value.currentPage + 1
            when (val result = networkRepository.searchVinyl(
                query = _state.value.searchQuery,
                type = _state.value.searchType,
                perPage = 20,
                page = nextPage
            )) {
                is Resource.Success -> {
                    val newResults =
                        result.value?.results?.map { it.toUiModel(isMaster = _state.value.searchType == SearchType.MASTER) }
                            ?: emptyList()
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
