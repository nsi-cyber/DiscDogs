package com.discdogs.app.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.VEButton
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.background_vinyl
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel) {

    val state by viewModel.state.collectAsState()

    val rotation by animateFloatAsState(
        targetValue = when (state.currentPageIndex % 3) {
            0 -> 0f
            1 -> 120f
            2 -> 240f
            else -> 360f
        },
        animationSpec = tween(durationMillis = 800),
        label = "rotationAnimation"
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VETheme.colors.backgroundColorPrimary),
    ) {
        Box(modifier = Modifier.drawWithContent {
            drawContent() // Önce resmi çiz
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.6f),
                        Color.Black.copy(alpha = 1f),

                        ),
                    startY = 0f,
                    endY = size.height
                )
            )
        }) {
            Image(
                painter = painterResource(Res.drawable.background_vinyl),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        x = (500 / 2.2).dp,
                        y = -(1000 / 2.2).dp
                    )
                    .scale(2.6f)
                    .rotate(rotation),

                )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with
                            slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                },
                modifier = Modifier.fillMaxWidth()
            ) { targetText ->
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(state.currentPage.image),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth().height(300.dp)
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = targetText.title.asString(),
                        style = VETheme.typography.text26TextColor200W600
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),

                        text = targetText.desc.asString(),
                        style = VETheme.typography.text13TextColor100W500
                    )
                }
            }


            VEButton(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                    onClick = {
                        viewModel.process(OnboardingEvent.OnPageChange(state.currentPageIndex.inc()))

                    },
                    content = {
                        AnimatedContent(
                            targetState = state.currentPage,
                            transitionSpec = {
                                slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with
                                        slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { targetText ->
                            Text(
                                modifier = Modifier.fillMaxWidth().padding(13.dp),
                                text = targetText.button.asString(),
                                style = VETheme.typography.text16TextColor200W700,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                )

        }

    }
}

