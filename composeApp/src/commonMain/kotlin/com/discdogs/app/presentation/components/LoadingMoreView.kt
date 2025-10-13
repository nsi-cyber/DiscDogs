package com.discdogs.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.ic_loading
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadingMore() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_loading),
                contentDescription = "Loading",
                tint = VETheme.colors.whiteColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Loading more...",
                style = VETheme.typography.text14TextColor100W400
            )
        }
    }
}