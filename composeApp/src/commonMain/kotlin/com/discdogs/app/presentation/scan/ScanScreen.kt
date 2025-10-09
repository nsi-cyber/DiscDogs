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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.discdogs.app.core.presentation.theme.VETheme
import discdogs.composeapp.generated.resources.Res
import discdogs.composeapp.generated.resources.ic_camera
import discdogs.composeapp.generated.resources.ic_flash
import org.jetbrains.compose.resources.painterResource


@Composable
fun ScanScreen(
    viewModel: ScanViewModel
) {
    rememberCoroutineScope()
    val state by viewModel.state.collectAsState()


    // Flash state
    var isFlashOn by remember { mutableStateOf(false) }

    // Track permission request attempts
    remember { mutableStateOf(0) }


    val hasCamPermission = true


    LocalLifecycleOwner.current







    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {

            //Camera Preview would be here

            CameraPreviewOverlay(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.isLoading,
                selectedScanType = state.selectedScanType,
                onScanTypeChanged = {
                    viewModel.process(
                        ScanEvent.OnSelectedScanTypeChanged(it)
                    )
                },
                showImageScanningOption = state.showImageScanningOption,
                isFlashOn = isFlashOn,
                onFlashToggle = { isFlashOn = !isFlashOn },
                onPhoto = {

                })
        } else {
            // Show permission denied view
            CameraPermissionDeniedView(
                onGivePermission = {
                    // Check if we should show rationale (permission not permanently denied)
                }
            )
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
    showImageScanningOption: Boolean = true,
    isFlashOn: Boolean = false,
    onFlashToggle: () -> Unit = {}
) {

    Box(modifier = modifier.padding(top = 34.dp, bottom = 100.dp)) {
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
                        text = "scan_barcode",
                        style = VETheme.typography.text14TextColor200W400
                    )
                }

                if (showImageScanningOption) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (selectedScanType == ScanType.IMAGE) VETheme.colors.primaryColor600 else Color.Transparent)
                            .clickable {
                                onScanTypeChanged(ScanType.IMAGE)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "scan_cover_image",
                            style = VETheme.typography.text14TextColor200W400
                        )
                    }
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

@Composable
fun CameraPermissionDeniedView(
    onGivePermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VETheme.colors.blackColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Camera icon
        Icon(
            painter = painterResource(Res.drawable.ic_camera),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 24.dp),
            tint = VETheme.colors.textColor200
        )

        // Title
        Text(
            text = "camera_permission_required",
            style = VETheme.typography.text16TextColor50W400,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Description
        Text(
            text = "camera_permission_description",
            style = VETheme.typography.text16TextColor200W400,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        // Give Permission Button
        Button(
            onClick = onGivePermission,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VETheme.colors.primaryColor600
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "give_permission",
                style = VETheme.typography.text16TextColor50W400,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
