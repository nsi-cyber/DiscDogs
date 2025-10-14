package com.discdogs.app.presentation.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.shimmerEffect
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.domain.SearchType
import com.discdogs.app.presentation.components.LoadingMore
import com.discdogs.app.presentation.components.SquareReleaseItemView
import com.discdogs.app.presentation.model.PageState
import com.discdogs.app.presentation.model.VinylResultUiModel
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.clear
import discdog.composeapp.generated.resources.ic_filter
import discdog.composeapp.generated.resources.ic_loading
import discdog.composeapp.generated.resources.ic_search
import discdog.composeapp.generated.resources.image_not_found
import discdog.composeapp.generated.resources.keep_hunting_try_to_search_with_different_keywords
import discdog.composeapp.generated.resources.no_results_found
import discdog.composeapp.generated.resources.recent_scans
import discdog.composeapp.generated.resources.recent_searches
import discdog.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val state by viewModel.state.collectAsState()
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()

    // Scroll-based pagination
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItem = visibleItems.last()
                    val totalItems = state.resultList?.size ?: 0

                    // Load more when user is within 3 items of the end
                    if (lastVisibleItem.index >= totalItems - 3 &&
                        state.hasMore &&
                        !state.isLoadingMore &&
                        state.searchQuery.isNotEmpty()
                    ) {
                        viewModel.process(SearchEvent.LoadMore)
                    }
                }
            }
    }
    
    Scaffold(
        containerColor = VETheme.colors.backgroundColorPrimary,
    ) { padd ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(padd),
            horizontalAlignment = Alignment.End
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            color = VETheme.colors.cardBackgroundColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .animateContentSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(Res.drawable.ic_search),
                            contentDescription = "Search Icon",
                            tint = VETheme.colors.whiteColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        BasicTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.process(SearchEvent.OnSearchQuery(it)) },
                            singleLine = true,
                            textStyle = VETheme.typography.text14TextColor200W500,
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.search),
                                        style = VETheme.typography.text14TextColor100W400
                                    )
                                }
                                innerTextField()
                            }
                        )

                    }
                }

                AnimatedVisibility(
                    visible = state.searchQuery.isNotEmpty(),
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { viewModel.process(SearchEvent.OnClearQuery) }
                        ) {
                            Text(
                                text = stringResource(Res.string.clear),
                                style = VETheme.typography.text14TextColor100W400
                            )
                        }
                    }
                }
            }

            Box {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable {
                        isDropDownExpanded.value = true
                    }.padding(4.dp)
                ) {

                    Text(
                        text = stringResource(state.searchType.title),
                        style = VETheme.typography.text16TextColor100W500,
                    )

                    Icon(
                        painter = painterResource(Res.drawable.ic_filter),
                        contentDescription = "Filter Icon",
                        tint = VETheme.colors.whiteColor,
                        modifier = Modifier
                            .size(24.dp)
                    )

                }
                DropdownMenu(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    containerColor = VETheme.colors.cardBackgroundColor,
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = {
                        isDropDownExpanded.value = false
                    }) {
                    SearchType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(type.title),
                                    style = VETheme.typography.text16TextColor200W400,
                                )
                            },
                            onClick = {
                                isDropDownExpanded.value = false

                                viewModel.process(SearchEvent.OnSearchTypeChanged(type))

                            })
                    }


                }
            }
            AnimatedContent(
                targetState = when {
                    state.isLoading -> PageState.LOADING
                    state.searchQuery.isNotEmpty() && state.resultList.isNullOrEmpty() -> PageState.EMPTY
                    state.searchQuery.isEmpty() -> PageState.IDLE
                    else -> PageState.SUCCESS
                },
                label = "ContentTransition",
                modifier = Modifier.fillMaxSize()
            ) { target ->
                when (target) {
                    PageState.LOADING -> ShimmeredLoadingState()

                    PageState.EMPTY -> NoResultView()

                    PageState.SUCCESS -> LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 62.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.resultList.orEmpty(), key = { it.id }) { vinyl ->
                            VinylItemView(data = vinyl, onClick = {
                                viewModel.process(SearchEvent.OnItemClicked(vinyl))
                            })
                        }

                        // Loading indicator at the bottom when loading more
                        if (state.isLoadingMore) {
                            item {
                                LoadingMore()
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                end = 8.dp,
                                top = 8.dp,
                                bottom = 62.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (state.recentSearchedReleases?.isNotEmpty() == true) {
                                item {
                                    Text(
                                        text = stringResource(Res.string.recent_searches),
                                        style = VETheme.typography.text16TextColor200W400,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                                item {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        items(
                                            state.recentSearchedReleases.orEmpty(),
                                            key = { it.id }) { vinyl ->
                                            SquareReleaseItemView(data = vinyl, onClick = {
                                                viewModel.process(SearchEvent.OnItemClicked(vinyl))
                                            })
                                        }

                                    }
                                }
                            }

                            if (state.recentScannedReleases?.isNotEmpty() == true) {
                                item {
                                    Text(
                                        text = stringResource(Res.string.recent_scans),
                                        style = VETheme.typography.text16TextColor200W400,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                                item {

                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),

                                        ) {
                                        items(
                                            state.recentScannedReleases.orEmpty(),
                                            key = { it.id }) { vinyl ->
                                            SquareReleaseItemView(data = vinyl, onClick = {
                                                viewModel.process(SearchEvent.OnItemClicked(vinyl))
                                            })
                                        }

                                    }

                                }
                            }

                        }


                    }
                }
            }


        }
    }


}

@Composable
private fun ShimmeredLoadingState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VinylItemViewShimmered()
        VinylItemViewShimmered()
        VinylItemViewShimmered()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VinylItemView(data: VinylResultUiModel, onClick: () -> Unit) {
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
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = data.thumb, contentDescription = "", modifier = Modifier
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

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VinylItemViewShimmered() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(true)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .shimmerEffect(true)
            )
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.4f)
                    .clip(CircleShape)
                    .shimmerEffect(true)
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(30.dp)
                    .clip(CircleShape)
                    .shimmerEffect(true)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(30.dp)
                        .clip(CircleShape)
                        .background(VETheme.colors.primaryColor500)
                        .shimmerEffect(true)
                )
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(30.dp)
                        .clip(CircleShape)
                        .background(VETheme.colors.primaryColor500)
                        .shimmerEffect(true)
                )

            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(20.dp)
                        .clip(CircleShape)
                        .background(VETheme.colors.cardBackgroundColor)
                        .shimmerEffect(true)
                )
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(20.dp)
                        .clip(CircleShape)
                        .background(VETheme.colors.cardBackgroundColor)
                        .shimmerEffect(true)
                )

            }

        }

    }
}

@Composable
private fun NoResultView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.image_not_found),
            contentDescription = "No Result",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colorFilter = ColorFilter.tint(VETheme.colors.primaryColor500)
        )
        Text(
            text = stringResource(Res.string.no_results_found),
            style = VETheme.typography.text16TextColor200W400,
        )
        Text(
            text = stringResource(Res.string.keep_hunting_try_to_search_with_different_keywords),
            style = VETheme.typography.text14TextColor100W400,
            textAlign = TextAlign.Center

        )
    }
}

