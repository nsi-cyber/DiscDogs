package com.discdogs.app.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource


@Composable
fun SplashScreen(
    viewModel: SplashViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.process(SplashEvent.OnInitialize)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VETheme.colors.backgroundColorPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp),

            )
    }
}
