package com.discdogs.app.presentation.scan

import com.discdogs.app.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ScanViewModel(


) : BaseViewModel<ScanState, ScanEffect, ScanEvent, ScanNavigator>() {

    private val _state = MutableStateFlow(ScanState())
    override val state: StateFlow<ScanState> get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ScanEffect>()
    override val effect: SharedFlow<ScanEffect> get() = _effect


    override fun process(event: ScanEvent) {
        when (event) {
            ScanEvent.OnBackClicked -> navigator?.navigateBack()
            is ScanEvent.OnSelectedScanTypeChanged -> _state.update { it.copy(selectedScanType = event.scanType) }
            //  is ScanEvent.OnImageCaptured -> handleBarcodeRecognition(event.imageProxy)
            //  is ScanEvent.OnPhotoCaptured -> processImage(event.bitmap)
        }
    }
    /*
        private fun handleBarcodeRecognition(imageProxy: ImageProxy) {
            viewModelScope.launch {
                try {
                    scanBarcodeUseCase(imageProxy)?.let { barcode ->
                        _state.update { it.copy(isLoading = true) }

                        when (val result = getVinylReleaseIdFromBarcodeUseCase(barcode)) {
                            is Resource.Success -> {
                                _state.update { it.copy(isLoading = false) }
                                result.value?.let {
                                    // Log analytics event for successful barcode scan
                                    analyticsService.logBarcodeScanned(barcode)
                                    navigator?.navigateToReleaseDetail(it)
                                    delay(2000)
                                }
                            }

                            is Resource.Error -> {
                                errorSnack(
                                    UiText.RawString(
                                        result.message.orEmpty()
                                    )
                                )
                                _state.update { it.copy(isLoading = false) }
                            }
                        }
                    }
                } catch (e: Exception) { } finally {
                    imageProxy.close()
                }
            }
        }

        fun processImage(bitmap: Bitmap) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                when (val scanResult = scanImageUseCase(bitmap)) {
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        if (scanResult.value != null) {
                            analyticsService.logImageScanned()
                            navigator?.navigateToReleaseDetail(scanResult.value)
                            delay(2000)
                        } else {
                            errorSnack(UiText.StringResource(R.string.sorry_we_couldn_t_find_it))
                        }
                    }

                    is Resource.Error -> {
                        errorSnack(
                            UiText.RawString(
                                scanResult.message.orEmpty()
                            )
                        )
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }

     */
}
