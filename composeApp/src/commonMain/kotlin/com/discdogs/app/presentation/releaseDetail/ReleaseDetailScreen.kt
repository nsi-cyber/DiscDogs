package com.discdogs.app.presentation.releaseDetail


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.shimmerEffect
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.PagerWithSteppable
import com.discdogs.app.presentation.components.PlayPreviewView
import com.discdogs.app.presentation.components.TrackItemView
import com.discdogs.app.presentation.components.bottomsheets.BarcodeBottomSheet
import com.discdogs.app.presentation.components.bottomsheets.CreateListBottomSheet
import com.discdogs.app.presentation.components.bottomsheets.ResultDetailMoreBottomSheet
import com.discdogs.app.presentation.components.bottomsheets.SaveToListBottomSheet
import com.discdogs.app.presentation.model.PageState
import com.discdogs.app.presentation.model.VinylDetailUiModel
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.disc_count
import discdog.composeapp.generated.resources.formats
import discdog.composeapp.generated.resources.genres
import discdog.composeapp.generated.resources.ic_chevron_left
import discdog.composeapp.generated.resources.ic_loading
import discdog.composeapp.generated.resources.ic_star_empty
import discdog.composeapp.generated.resources.ic_star_filled
import discdog.composeapp.generated.resources.ic_three_dots
import discdog.composeapp.generated.resources.release_date
import discdog.composeapp.generated.resources.show_less
import discdog.composeapp.generated.resources.show_more
import discdog.composeapp.generated.resources.styles
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReleaseDetailScreen(
    viewModel: ReleaseDetailViewModel
) {
    val moreBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val barcodeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val createListBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                ReleaseDetailEffect.DismissMoreBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.hide()
                    }
                }

                ReleaseDetailEffect.ShowMoreBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.show()
                    }
                }

                ReleaseDetailEffect.DismissBarcodeBottomSheet -> {
                    scope.launch {
                        barcodeBottomSheetState.hide()
                    }
                }

                ReleaseDetailEffect.ShowBarcodeBottomSheet -> {
                    scope.launch {
                        barcodeBottomSheetState.show()
                    }
                }

                ReleaseDetailEffect.ShowSaveToListBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.show()
                    }
                }

                ReleaseDetailEffect.DismissSaveToListBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.hide()
                    }
                }

                else -> {


                }

            }

        }
    }

    val pages = remember(state.releaseDetail?.images) {
        state.releaseDetail?.images?.map { image ->
            @Composable {
                AsyncImage(
                    model = image.resourceUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 60.dp)
                        .aspectRatio(1f),
                    placeholder = painterResource(Res.drawable.ic_loading)
                )
            }
        } ?: emptyList()
    }
    var firstItemHeight by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState()
    val scrollOffset = remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
    val firstVisibleIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    val rawRatio = if (firstVisibleIndex.value == 0 && firstItemHeight > 0) {
        (firstItemHeight - scrollOffset.value).toFloat() / firstItemHeight
    } else {
        0f
    }

    val clampedRatio = rawRatio.coerceIn(0f, 1f)

    val animatedRatio by animateFloatAsState(
        targetValue = clampedRatio,
        animationSpec = tween(durationMillis = 50),
        label = "animatedRatio"
    )

    val alpha = animatedRatio

    Scaffold(bottomBar = {
        PlayPreviewView(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 80.dp),
            data = state.playingItem,
            isLoading = state.isPreviewLoading,
            color = 0xFF1A1A1A.toInt(),
            onStop = {
                viewModel.process(ReleaseDetailEvent.OnReleaseTrack)
            }
        )
    }) { padding ->
        AsyncImage(
            model = state.backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().blur(30.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            VETheme.colors.backgroundColorPrimary.copy(0.6f),
                            VETheme.colors.backgroundColorPrimary,
                        )
                    )
                )
        )
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            viewModel.process(ReleaseDetailEvent.OnBackClicked)
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
                    text = state.releaseDetail?.title.orEmpty(),
                    style = VETheme.typography.text14TextColor200W600,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(1f - alpha)
                        .padding(end = 32.dp),
                    textAlign = TextAlign.Center
                )


            }

            AnimatedContent(
                targetState = when {
                    state.isLoading -> PageState.LOADING
                    else -> PageState.SUCCESS
                },
                label = "ContentTransition",
                modifier = Modifier.fillMaxSize()
            ) { target ->
                when (target) {

                    PageState.SUCCESS -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(top = 24.dp, bottom = 140.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        item {
                            PagerWithSteppable(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        firstItemHeight = coordinates.size.height
                                    },
                                pages = pages
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .animateContentSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Text(
                                    text = state.releaseDetail?.title.orEmpty(),
                                    style = VETheme.typography.text34TextColor200W600,
                                )

                                Text(
                                    text = state.releaseDetail?.artists?.joinToString(", ") { it.name }
                                        .orEmpty(),
                                    style = VETheme.typography.text14TextColor200W400,
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable {

                                                viewModel.process(ReleaseDetailEvent.OnShowSaveToListBottomSheet)

                                            }) {
                                        Icon(
                                            painter = if (state.isFavorite) painterResource(Res.drawable.ic_star_filled) else painterResource(
                                                Res.drawable.ic_star_empty
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(24.dp),
                                            tint = VETheme.colors.textColor100
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable {
                                                viewModel.process(ReleaseDetailEvent.OnShowMoreBottomSheet)
                                            }) {
                                        Icon(
                                            painter = painterResource(Res.drawable.ic_three_dots),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(24.dp),
                                            tint = VETheme.colors.textColor100
                                        )
                                    }
                                }

                                ReleaseInfoDetailView(state.releaseDetail)

                            }
                        }
                        items(state.releaseDetail?.trackList.orEmpty(), key = { it.id }) { data ->
                            TrackItemView(
                                data = data,
                                isPlaying = data.id == state.playingItem?.id,
                                onClick = {
                                    viewModel.process(ReleaseDetailEvent.OnPreviewTrack(data))
                                })
                        }
                    }

                    else -> ReleaseDetailViewShimmered()

                }
            }


        }


    }




    if (state.moreSheetVisible) {
        ResultDetailMoreBottomSheet(
            sheetState = moreBottomSheetState,
            onDismiss = { viewModel.process(ReleaseDetailEvent.OnDismissMoreBottomSheet) },
            onShare = {
                viewModel.process(ReleaseDetailEvent.OnShare)

            },
            onExternal = { type -> viewModel.process(ReleaseDetailEvent.OnExternalWebsite(type)) },
            hasBarcode = !state.releaseDetail?.barcode.isNullOrEmpty(),
            onBarcode = {
                viewModel.process(ReleaseDetailEvent.OnDismissMoreBottomSheet)
                viewModel.process(ReleaseDetailEvent.OnShowBarcodeBottomSheet)
            },
            hasMaster = state.releaseDetail?.masterId != -1,
            onReleases = {
                viewModel.process(ReleaseDetailEvent.OnDismissMoreBottomSheet)
                viewModel.process(ReleaseDetailEvent.OnOtherReleases)
            },
        )
    }
    if (state.barcodeSheetVisible) {
        BarcodeBottomSheet(
            sheetState = moreBottomSheetState,
            onDismiss = {
                viewModel.process(ReleaseDetailEvent.OnDismissBarcodeBottomSheet)
            },
            barcode = state.releaseDetail?.barcode
        )
    }
    if (state.saveToListSheetVisible) {
        SaveToListBottomSheet(
            sheetState = moreBottomSheetState,
            onDismiss = { viewModel.process(ReleaseDetailEvent.OnDismissSaveToListBottomSheet) },
            lists = state.lists,
            isFavorite = state.isFavorite,
            releaseInLists = state.releaseInLists,
            onToggleFavorite = { viewModel.process(ReleaseDetailEvent.OnToggleFavorite) },
            onAddToList = { listId -> viewModel.process(ReleaseDetailEvent.OnAddToList(listId)) },
            onCreateNewList = { viewModel.process(ReleaseDetailEvent.OnCreateNewList) }
        )
    }
    // Create List Bottom Sheet
    if (state.showCreateListBottomSheet) {
        CreateListBottomSheet(
            sheetState = createListBottomSheetState,
            onDismiss = {
                scope.launch {
                    createListBottomSheetState.hide()
                }
                viewModel.process(ReleaseDetailEvent.OnDismissCreateListBottomSheet)
            },
            onConfirm = { listName ->
                viewModel.process(ReleaseDetailEvent.OnCreateList(listName))
            }
        )
    }


}




@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReleaseDetailViewShimmered() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .aspectRatio(1f)
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReleaseInfoDetailView(data: VinylDetailUiModel?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VETheme.colors.cardBackgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        data?.releasedFormatted?.let {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.release_date),
                    style = VETheme.typography.text14TextColor100W400,
                )
                Text(
                    text = data.releasedFormatted,
                    style = VETheme.typography.text14TextColor200W400,
                )
            }
        }

        data?.genres?.let {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.genres),
                    style = VETheme.typography.text14TextColor100W400,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.genres.forEach { text ->
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VETheme.colors.primaryColor500)
                                .padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                ),
                            text = text,
                            style = VETheme.typography.text14TextColor200W400,
                        )

                    }
                }

            }
        }

        data?.styles?.takeIf { it.isNotEmpty() }?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.styles),
                    style = VETheme.typography.text14TextColor100W400,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.styles.forEach { text ->
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VETheme.colors.primaryColor600)
                                .padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                ),
                            text = text,
                            style = VETheme.typography.text14TextColor200W400,
                        )

                    }
                }
            }
        }

        data?.formats?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.formats),
                    style = VETheme.typography.text14TextColor100W400,
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.formats.forEach { format ->
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VETheme.colors.primaryColor700)
                                .padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                ),
                            text = format.name,
                            style = VETheme.typography.text14TextColor200W400,
                        )

                        format.descriptions?.forEach { text ->
                            Text(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(VETheme.colors.primaryColor700)
                                    .padding(
                                        horizontal = 6.dp,
                                        vertical = 2.dp
                                    ),
                                text = text,
                                style = VETheme.typography.text14TextColor200W400,
                            )

                        }
                    }
                }
            }


        }

        data?.formatQuantity?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.disc_count),
                    style = VETheme.typography.text14TextColor100W400,
                )


                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(VETheme.colors.primaryColor800)
                        .padding(
                            horizontal = 6.dp,
                            vertical = 2.dp
                        ),
                    text = data.formatQuantity.toString(),
                    style = VETheme.typography.text14TextColor200W400,
                )

            }
        }

        data?.notes?.let {
            var isOverflowing by remember { mutableStateOf(false) }
            var showMore by remember { mutableStateOf(false) }
            Text(
                text = data.notes,
                style = VETheme.typography.text14TextColor100W400,
                maxLines = if (!showMore) 2 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    isOverflowing = textLayoutResult.hasVisualOverflow
                }
            )

            if (isOverflowing || showMore) {
                Text(
                    text = if (showMore) stringResource(Res.string.show_less) else stringResource(
                        Res.string.show_more
                    ),
                    style = VETheme.typography.text14TextColor200W400.copy(
                        color = VETheme.colors.primaryColor500
                    ),
                    modifier = Modifier
                        .clickable {
                            showMore = !showMore
                        }
                )
            }
        }

    }

}







