package com.discdogs.app.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.discdogs.app.app.Route
import com.discdogs.app.core.presentation.UiText
import com.discdogs.app.core.presentation.theme.VETheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Bottom navigation item model
 */
data class VEBottomNavigationItem(
    val route: Route,
    val title: UiText,
    val icon: DrawableResource,
)

/**
 * NavigationBar component with NavController + restoreState support
 */
@Composable
fun VEBottomNavigationBar(
    currentRoute: String,
    navigateTo: (Route) -> Unit,
    returnToStart: (Route) -> Unit,
    items: List<VEBottomNavigationItem>,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        VETheme.colors.backgroundColorPrimary.copy(alpha = 0.8f),
                        VETheme.colors.backgroundColorPrimary
                    )
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute.contains(item.route.toString())

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 32.dp)
                    .clip(CircleShape)
                    .padding(4.dp)
                    .clickable {
                        if (!currentRoute.contains(item.route.toString())) {
                            navigateTo(item.route)
                        } else {
                            returnToStart(item.route)
                        }
                    }
                    .padding(16.dp)

            ) {

                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) VETheme.colors.whiteColor else VETheme.colors.whiteColor.copy(
                        alpha = 0.5f
                    )
                )

                Text(
                    text = item.title.asString(),
                    style = VETheme.typography.text14TextColor200W400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}
