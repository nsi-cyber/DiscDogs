package com.discdogs.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.model.PlayingItem
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.ic_stop
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayPreviewView(
    modifier: Modifier,
    isLoading: Boolean,
    data: PlayingItem?,
    color: Int,
    onStop: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier.pointerInput(Unit) {},
        visible = data != null,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(color))
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = data?.image, contentDescription = "", modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = data?.title.orEmpty(),
                style = VETheme.typography.text16TextColor200W400,
            )
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(CircleShape)
                    .clickable {
                        onStop()
                    }
                    .padding(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = VETheme.colors.textColor100,
                        trackColor = Color.Transparent
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_stop),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = VETheme.colors.textColor100

                    )
                }
            }
        }

    }

}