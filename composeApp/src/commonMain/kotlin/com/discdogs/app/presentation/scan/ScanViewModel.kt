package com.discdogs.app.presentation.scan

import androidx.lifecycle.viewModelScope
import com.discdogs.app.core.data.Resource
import com.discdogs.app.core.presentation.BaseViewModel
import com.discdogs.app.core.presentation.UiText
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.domain.SearchType
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.no_vinyl_record_found_in_the_image_please_try_again_with_a_clearer_image_or_different_angle
import discdog.composeapp.generated.resources.sorry_we_couldn_t_find_it
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
                        UiText.DynamicString(result.message.orEmpty())
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
                                        errorSnack(UiText.StringResourceId(Res.string.sorry_we_couldn_t_find_it))
                                        _state.update { it.copy(isLoading = false) }

                                    }

                                }

                                is Resource.Error -> {
                                    errorSnack(UiText.StringResourceId(Res.string.sorry_we_couldn_t_find_it))

                                    _state.update { it.copy(isLoading = false) }
                                }
                            }
                        } else {
                            _state.update { it.copy(isLoading = false) }

                            errorSnack(UiText.StringResourceId(Res.string.no_vinyl_record_found_in_the_image_please_try_again_with_a_clearer_image_or_different_angle))
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        errorSnack(UiText.StringResourceId(Res.string.no_vinyl_record_found_in_the_image_please_try_again_with_a_clearer_image_or_different_angle))
                    }


                }

                is Resource.Error -> {
                    errorSnack(
                        UiText.DynamicString(result.message ?: "Failed to generate image caption")
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }


}
