package com.discdogs.app.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.presentation.components.bottomsheets.CreateListBottomSheet
import com.discdogs.app.presentation.listdetail.EmptyListState
import com.discdogs.app.presentation.model.VinylResultUiModel
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.create_new_list
import discdog.composeapp.generated.resources.favorites
import discdog.composeapp.generated.resources.ic_chevron_left
import discdog.composeapp.generated.resources.ic_delete
import discdog.composeapp.generated.resources.ic_loading
import discdog.composeapp.generated.resources.ic_plus
import discdog.composeapp.generated.resources.library
import discdog.composeapp.generated.resources.lists
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel
) {
    val state by viewModel.state.collectAsState()
    val createListBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = VETheme.colors.backgroundColorPrimary,
    ) { padd ->
        Column(modifier = Modifier.fillMaxSize().padding(padd)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.library),
                    style = VETheme.typography.text14TextColor200W600,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Tabs
            TabRow(
                selectedTabIndex = if (state.selectedTab == LibraryTab.FAVORITES) 0 else 1,
                modifier = Modifier.fillMaxWidth(),
                containerColor = VETheme.colors.backgroundColorPrimary,
                contentColor = VETheme.colors.textColor200,
                indicator = { tabPositions ->
                    val selectedIndex = if (state.selectedTab == LibraryTab.FAVORITES) 0 else 1
                    if (selectedIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[selectedIndex]
                            ), color = VETheme.colors.primaryColor500
                        )

                    }
                }
            ) {
                Tab(
                    selected = state.selectedTab == LibraryTab.FAVORITES,
                    onClick = { viewModel.process(LibraryEvent.OnFavoritesTabSelected) },
                    text = {
                        Text(
                            stringResource(Res.string.favorites),
                            color = if (state.selectedTab == LibraryTab.FAVORITES) {
                                VETheme.colors.primaryColor500
                            } else {
                                VETheme.colors.textColor100
                            }
                        )
                    }
                )
                Tab(
                    selected = state.selectedTab == LibraryTab.LISTS,
                    onClick = { viewModel.process(LibraryEvent.OnListsTabSelected) },
                    text = {
                        Text(
                            stringResource(Res.string.lists),
                            color = if (state.selectedTab == LibraryTab.LISTS) {
                                VETheme.colors.primaryColor500
                            } else {
                                VETheme.colors.textColor100
                            }
                        )
                    }
                )
            }

            // Content
            when (state.selectedTab) {
                LibraryTab.FAVORITES -> {
                    FavoritesTabContent(
                        releases = state.favoriteReleases,
                        onReleaseClick = { release ->
                            viewModel.process(LibraryEvent.OnReleaseDetail(release))
                        },
                        onRemoveFromFavorites = { releaseId ->
                            viewModel.process(LibraryEvent.OnRemoveFromFavorites(releaseId))
                        }
                    )
                }

                LibraryTab.LISTS -> {
                    ListsTabContent(
                        lists = state.lists,
                        onCreateNewList = { viewModel.process(LibraryEvent.OnCreateNewList) },
                        onListSelected = { listId ->
                            viewModel.process(
                                LibraryEvent.OnListSelected(
                                    listId
                                )
                            )
                        },
                        onDeleteList = { listId ->
                            viewModel.process(
                                LibraryEvent.OnDeleteList(
                                    listId
                                )
                            )
                        }
                    )
                }
            }
        }

    }


    // Create List Bottom Sheet
    if (state.showCreateListBottomSheet) {
        CreateListBottomSheet(
            sheetState = createListBottomSheetState,
            onDismiss = {
                scope.launch {
                    createListBottomSheetState.hide()
                }
                viewModel.process(LibraryEvent.OnDismissCreateListBottomSheet)
            },
            onConfirm = { listName ->
                viewModel.process(LibraryEvent.OnCreateList(listName))
            }
        )
    }
}

@Composable
private fun FavoritesTabContent(
    releases: List<VinylResultUiModel>?,
    onReleaseClick: (VinylResultUiModel) -> Unit,
    onRemoveFromFavorites: (Int) -> Unit
) {
    if (!releases.isNullOrEmpty()) {
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
            items(releases.orEmpty(), key = { it.id }) { vinyl ->
                VinylItemView(
                    data = vinyl,
                    onClick = { onReleaseClick(vinyl) },
                    onRemove = { onRemoveFromFavorites(vinyl.id) }
                )
            }
        }
    } else
        EmptyListState()
}

@Composable
private fun ListsTabContent(
    lists: List<ReleaseList>?,
    onCreateNewList: () -> Unit,
    onListSelected: (Long) -> Unit,
    onDeleteList: (Long) -> Unit
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
        items(lists.orEmpty(), key = { it.id }) { list ->
            ListItemView(
                list = list,
                onClick = { onListSelected(list.id) },
                onDelete = { onDeleteList(list.id) }
            )
        }

        // Create new list button
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onCreateNewList() },
                colors = CardDefaults.cardColors(
                    containerColor = VETheme.colors.cardBackgroundColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = VETheme.colors.primaryColor500
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(Res.string.create_new_list),
                        style = VETheme.typography.text16TextColor200W400,
                        color = VETheme.colors.primaryColor500
                    )
                }
            }
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
                contentDescription = "Remove from favorites",
                tint = VETheme.colors.redColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ListItemView(
    list: ReleaseList,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = VETheme.colors.cardBackgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_left),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = VETheme.colors.textColor200
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = list.name,
                    style = VETheme.typography.text16TextColor200W400
                )

            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Delete list",
                    tint = VETheme.colors.redColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


