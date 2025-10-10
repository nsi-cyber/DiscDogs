package com.discdogs.app.presentation.listdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.model.VinylResultUiModel
import discdogs.composeapp.generated.resources.Res
import discdogs.composeapp.generated.resources.ic_chevron_left
import discdogs.composeapp.generated.resources.ic_delete
import discdogs.composeapp.generated.resources.ic_empty_drawer
import discdogs.composeapp.generated.resources.ic_loading
import org.jetbrains.compose.resources.painterResource

@Composable
fun ListDetailScreen(
    viewModel: ListDetailViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 42.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        viewModel.process(ListDetailEvent.OnBackClicked)
                    }) {
                Image(
                    painter = painterResource(Res.drawable.ic_chevron_left),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)

                )
            }

            Text(
                text = state.listName.orEmpty(),
                style = VETheme.typography.text14TextColor200W600,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 32.dp),
                textAlign = TextAlign.Center
            )
        }


        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = VETheme.colors.primaryColor500
                    )
                }
            }

            state.releases.isNullOrEmpty() -> {
                EmptyListState()
            }

            else -> {
                ListReleasesContent(
                    releases = state.releases!!,
                    onReleaseClick = { release ->
                        viewModel.process(ListDetailEvent.OnReleaseClick(release))
                    },
                    onRemoveRelease = { releaseId ->
                        viewModel.process(ListDetailEvent.OnRemoveRelease(releaseId))
                    }
                )
            }
        }
    }
}

@Composable
private fun ListReleasesContent(
    releases: List<VinylResultUiModel>,
    onReleaseClick: (VinylResultUiModel) -> Unit,
    onRemoveRelease: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 16.dp,
            bottom = 62.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(releases, key = { it.id }) { vinyl ->
            VinylItemView(
                data = vinyl,
                onClick = { onReleaseClick(vinyl) },
                onRemove = { onRemoveRelease(vinyl.id) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VinylItemView(
    data: VinylResultUiModel,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = data.thumb,
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(Res.drawable.ic_loading)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = data.title,
                style = VETheme.typography.text16TextColor200W400.copy(
                    color = VETheme.colors.textColor200
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = data.year,
                style = VETheme.typography.text12TextColor100W400,
            )
            data.format?.let {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.format.forEach { format ->
                        Text(
                            text = format,
                            style = VETheme.typography.text12TextColor200W400,
                            modifier = Modifier
                                .background(
                                    color = VETheme.colors.primaryColor500,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            data.genre?.let {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.genre.forEach { genre ->
                        Text(
                            text = genre,
                            style = VETheme.typography.text12TextColor100W400,
                            modifier = Modifier
                                .background(
                                    color = VETheme.colors.cardBackgroundColor,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Delete icon
        IconButton(
            onClick = onRemove,
            modifier = Modifier.padding(6.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = "Remove from list",
                tint = VETheme.colors.redColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun EmptyListState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_empty_drawer),
            contentDescription = "No Result",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colorFilter = ColorFilter.tint(VETheme.colors.primaryColor500)
        )
        Text(
            text = "stringResource(R.string.there_are_no_records_here_yet)",
            style = VETheme.typography.text16TextColor200W400,
        )
        Text(
            text = "stringResource(R.string.save_your_records_to_not_lose_them)",
            style = VETheme.typography.text14TextColor100W400,
            textAlign = TextAlign.Center

        )
    }
}
