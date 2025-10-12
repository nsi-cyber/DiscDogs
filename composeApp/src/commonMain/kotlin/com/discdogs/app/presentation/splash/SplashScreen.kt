package com.discdogs.app.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.ic_vinyl_detailed
import org.jetbrains.compose.resources.painterResource


@Composable
fun SplashScreen(
    viewModel: SplashViewModel
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAnimation"
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = VETheme.colors.backgroundColorPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_vinyl_detailed),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .rotate(rotation.value),
            colorFilter = ColorFilter.tint(VETheme.colors.primaryColor500)

        )
    }
}
