package com.discdogs.app.presentation.components.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.model.ExternalWebsites
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.ic_barcode
import discdog.composeapp.generated.resources.ic_share
import discdog.composeapp.generated.resources.ic_vinyl_format
import discdog.composeapp.generated.resources.icon_apple_music
import discdog.composeapp.generated.resources.icon_spotify
import discdog.composeapp.generated.resources.icon_youtube_music
import discdog.composeapp.generated.resources.share
import discdog.composeapp.generated.resources.show_barcode
import discdog.composeapp.generated.resources.show_on_apple_music
import discdog.composeapp.generated.resources.show_on_spotify
import discdog.composeapp.generated.resources.show_on_youtube_music
import discdog.composeapp.generated.resources.show_other_releases
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDetailMoreBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit = {},
    hasMaster: Boolean = false,
    onReleases: () -> Unit = {},
    onShare: () -> Unit = {},
    onExternal: (type: ExternalWebsites) -> Unit = {},
    hasBarcode: Boolean = false,
    onBarcode: () -> Unit = {},
) {

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = VETheme.colors.backgroundColorPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (hasBarcode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onBarcode()
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_barcode),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(VETheme.colors.textColor100)
                    )
                    Text(
                        text = stringResource(Res.string.show_barcode),
                        style = VETheme.typography.text16TextColor200W500,
                    )

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onShare()
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp),
                    colorFilter = ColorFilter.tint(VETheme.colors.textColor100)
                )
                Text(
                    text = stringResource(Res.string.share),
                    style = VETheme.typography.text16TextColor200W500,
                )

            }

            if (hasMaster) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onReleases()
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_vinyl_format),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(VETheme.colors.textColor100)
                    )
                    Text(
                        text = stringResource(Res.string.show_other_releases),
                        style = VETheme.typography.text16TextColor200W500,
                    )

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onExternal(ExternalWebsites.SPOTIFY)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_spotify),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp),
                )
                Text(
                    text = stringResource(Res.string.show_on_spotify),
                    style = VETheme.typography.text16TextColor200W500,
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onExternal(ExternalWebsites.APPLE_MUSIC)

                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_apple_music),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp),
                )
                Text(
                    text = stringResource(Res.string.show_on_apple_music),
                    style = VETheme.typography.text16TextColor200W500,
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onExternal(ExternalWebsites.YOUTUBE_MUSIC)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.icon_youtube_music),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp),
                )
                Text(
                    text = stringResource(Res.string.show_on_youtube_music),
                    style = VETheme.typography.text16TextColor200W500,
                )

            }
        }

    }

}