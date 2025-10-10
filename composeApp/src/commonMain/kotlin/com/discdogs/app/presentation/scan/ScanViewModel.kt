package com.discdogs.app.presentation.scan

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.domain.NetworkRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ScanViewModel(

    private val networkRepository: NetworkRepository
) : BaseViewModel<ScanState, ScanEffect, ScanEvent, ScanNavigator>() {

    private val _state = MutableStateFlow(ScanState())
    override val state: StateFlow<ScanState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ScanEffect>()
    override val effect: SharedFlow<ScanEffect> get() = _effect


    override fun process(event: ScanEvent) {
        when (event) {
            ScanEvent.OnBackClicked -> navigator?.navigateBack()
            is ScanEvent.OnSelectedScanTypeChanged -> _state.update { it.copy(selectedScanType = event.scanType) }
            is ScanEvent.OnPhotoCaptured -> handlePhotoCaptured(event.imageBytes)
            is ScanEvent.OnImageCaptured -> handlePhotoCaptured(event.imageBytes)
            is ScanEvent.OnBarcodeCaptured -> handleBarcodeCaptured(event.barcode)
        }
    }

    private fun handleBarcodeCaptured(barcode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = networkRepository.searchBarcode(barcode)) {
                is Resource.Success -> {
                    result.value?.let {
                        navigator?.navigateToReleaseDetail(it)
                        delay(2000)
                    }
                    _state.update { it.copy(isLoading = false) }

                }

                is Resource.Error -> {
                    errorSnack(
                        result.message.orEmpty()
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }


    private fun handlePhotoCaptured(imageBytes: ByteArray) {
        // Handle the captured photo
        // You can process the image here or save it
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            _state.update { it.copy(isLoading = false) }
        }
    }


}
