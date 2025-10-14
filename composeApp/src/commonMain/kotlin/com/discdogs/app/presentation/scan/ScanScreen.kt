package com.discdogs.app.presentation.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.camera.ScannerController
import com.discdogs.app.core.camera.ScannerView
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.VEButton
import dev.icerock.moko.permissions.PermissionState
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.camera_access_desc
import discdog.composeapp.generated.resources.camera_access_title
import discdog.composeapp.generated.resources.ic_camera
import discdog.composeapp.generated.resources.ic_flash
import discdog.composeapp.generated.resources.image_binoculars
import discdog.composeapp.generated.resources.scan_barcode
import discdog.composeapp.generated.resources.scan_cover_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScanScreen(
    viewModel: ScanViewModel
) {
    val state by viewModel.state.collectAsState()

    // Scanner controller for managing camera features
    val scannerController = remember { ScannerController() }

    // Flash state
    var isFlashOn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.process(ScanEvent.ProvidePermission(true))
    }

    Scaffold(containerColor = VETheme.colors.backgroundColorPrimary) { padd ->
        if (state.permissionState == PermissionState.Granted) {
            // Camera permission granted - show camera preview
            Box(modifier = Modifier.fillMaxSize()) {
                ScannerView(
                    isLoading = state.isLoading,
                    scannerController = scannerController,
                    result = { barcode ->
                        if (state.selectedScanType == ScanType.BARCODE && state.isLoading == false) {
                            viewModel.process(
                                ScanEvent.OnBarcodeCaptured(barcode)
                            )
                        }
                    },
                    onPhotoCaptured = { byteArray ->
                        if (state.selectedScanType == ScanType.IMAGE && state.isLoading == false) {
                            viewModel.process(
                                ScanEvent.OnPhotoCaptured(byteArray)
                            )
                        }
                    }
                )

                CameraPreviewOverlay(
                    modifier = Modifier.fillMaxSize().padding(padd),
                    isLoading = state.isLoading,
                    selectedScanType = state.selectedScanType,
                    onScanTypeChanged = {
                        viewModel.process(
                            ScanEvent.OnSelectedScanTypeChanged(it)
                        )
                    },
                    isFlashOn = isFlashOn,
                    onFlashToggle = {
                        isFlashOn = !isFlashOn
                        scannerController.setTorch(isFlashOn)
                    },
                    onPhoto = {
                        scannerController.capturePhoto()
                    })
            }
        } else {
            // Camera permission not granted - show permission request UI
            PermissionRequestView(
                onRequestPermission = {
                    viewModel.process(ScanEvent.ProvidePermission(false))
                }
            )
        }
    }
}

@Composable
fun PermissionRequestView(
    onRequestPermission: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {

                Icon(
                    painter = painterResource(Res.drawable.image_binoculars),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    tint = VETheme.colors.primaryColor600
                )


            // Title
            Text(
                text = stringResource(Res.string.camera_access_title),
                style = VETheme.typography.text18TextColor200W400,
                color = VETheme.colors.textColor200,
                textAlign = TextAlign.Center
            )

            // Description
            Text(
                text = stringResource(Res.string.camera_access_desc),
                style = VETheme.typography.text14TextColor200W400,
                color = VETheme.colors.textColor200,
                textAlign = TextAlign.Center
            )

            // Request permission button
            VEButton(
                onClick = onRequestPermission,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Allow Camera Access",
                    color = VETheme.colors.whiteColor,
                    style = VETheme.typography.text16TextColorWhiteW500
                )
            }
        }
    }
}

@Composable
fun CameraPreviewOverlay(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    selectedScanType: ScanType,
    onScanTypeChanged: (ScanType) -> Unit,
    onPhoto: () -> Unit,
    isFlashOn: Boolean = false,
    onFlashToggle: () -> Unit = {}
) {
    Box(modifier = modifier.padding(top = 16.dp, bottom = 100.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(VETheme.colors.blackColor)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (selectedScanType == ScanType.BARCODE) VETheme.colors.primaryColor600 else Color.Transparent)
                        .clickable {
                            onScanTypeChanged(ScanType.BARCODE)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.scan_barcode),
                        style = VETheme.typography.text14TextColor200W400
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (selectedScanType == ScanType.IMAGE) VETheme.colors.primaryColor600 else Color.Transparent)
                        .clickable {
                            onScanTypeChanged(ScanType.IMAGE)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = stringResource(Res.string.scan_cover_image),
                        style = VETheme.typography.text14TextColor200W400
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            if (selectedScanType == ScanType.IMAGE) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .border(3.dp, VETheme.colors.primaryColor600, CircleShape)
                        .background(Color.White)
                        .clickable {
                            if (isLoading == false)
                                onPhoto()
                        }
                        .padding(12.dp)) {
                    if (isLoading)
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            color = VETheme.colors.blackColor,
                            strokeWidth = 6.dp
                        )
                    else
                        Icon(
                            painter = painterResource(Res.drawable.ic_camera),
                            contentDescription = null,
                            tint = VETheme.colors.primaryColor600,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .alpha(if (isFlashOn) 1f else 0.5f)
                .clip(CircleShape)
                .background(VETheme.colors.blackColor.copy(alpha = 0.7f))
                .clickable { onFlashToggle() }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_flash),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = VETheme.colors.primaryColor600
            )
        }
    }
}