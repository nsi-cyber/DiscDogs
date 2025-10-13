package com.discdogs.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.model.TrackListUiModel


@Composable
fun TrackItemView(data: TrackListUiModel, isPlaying: Boolean, onClick: () -> Unit) {


    if (isPlaying) VETheme.colors.primaryColor500 else VETheme.colors.textColor100



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = data.title,
                style = VETheme.typography.text16TextColor200W400.copy(
                    color = if (isPlaying) VETheme.colors.primaryColor500 else VETheme.colors.textColor200
                ),
            )
            Text(
                text = data.position.orEmpty(),
                style = VETheme.typography.text12TextColor100W400,
            )

        }
        Text(
            text = data.duration.orEmpty(),
            style = VETheme.typography.text12TextColor100W400,
        )
    }
}

