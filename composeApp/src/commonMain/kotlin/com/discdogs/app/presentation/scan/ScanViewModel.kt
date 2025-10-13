package com.discdogs.app.presentation.scan

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.domain.SearchType
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
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


    private var _permissionsController: PermissionsController? = null
    val permissionsController: PermissionsController get() = _permissionsController!!


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
            is ScanEvent.SetPermissionController -> {
                _permissionsController = event.controller
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            permissionState = _permissionsController?.getPermissionState(Permission.CAMERA)
                                ?: PermissionState.NotGranted
                        )
                    }
                }

            }

            is ScanEvent.ProvidePermission -> requestCameraPermission(event.isFirstTime)
        }
    }

    private fun requestCameraPermission(isFirstTime: Boolean) {
        viewModelScope.launch {
            try {
                _permissionsController?.providePermission(Permission.CAMERA)
                _state.update { it.copy(permissionState = PermissionState.Granted) }
            } catch (e: DeniedAlwaysException) {
                if (_state.value.permissionState == PermissionState.DeniedAlways && isFirstTime == false) {
                    _permissionsController?.openAppSettings()
                }
                _state.update { it.copy(permissionState = PermissionState.DeniedAlways) }


            } catch (e: DeniedException) {
                _state.update { it.copy(permissionState = PermissionState.Denied) }

            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
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
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = networkRepository.generateImageCaption(
                imageBytes,
            )) {
                is Resource.Success -> {

                    if (result.value?.response == 200) {
                        if (result.value.name != null) {
                            val query = result.value.name
                            when (val res =
                                networkRepository.searchVinyl(query, type = SearchType.MASTER)) {
                                is Resource.Success -> {
                                    if (res.value?.firstOrNull()?.id != null) {
                                        _state.update { it.copy(isLoading = false) }
                                        navigator?.navigateToMasterDetail(
                                            res.value.firstOrNull()?.id!!,
                                            image = res.value.firstOrNull()?.thumb
                                        )
                                    } else {
                                        errorSnack("No vinyl record found with the name $query")
                                        _state.update { it.copy(isLoading = false) }

                                    }

                                }

                                is Resource.Error -> {
                                    errorSnack("No vinyl record found with the name $query")
                                    _state.update { it.copy(isLoading = false) }
                                }
                            }
                        } else {
                            _state.update { it.copy(isLoading = false) }

                            errorSnack("No vinyl record found in the image")
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }

                        errorSnack("No vinyl record found in the image")
                    }


                }

                is Resource.Error -> {
                    errorSnack(
                        result.message ?: "Failed to generate image caption"
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }


}
