package com.discdogs.app.presentation.detail


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.discdogs.app.core.presentation.shimmerEffect
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.EAN13BarcodeWithLabel
import com.discdogs.app.presentation.model.ExternalWebsites
import com.discdogs.app.presentation.model.PageState
import com.discdogs.app.presentation.model.TrackListUiModel
import com.discdogs.app.presentation.model.VinylDetailUiModel
import discdogs.composeapp.generated.resources.Res
import discdogs.composeapp.generated.resources.ic_barcode
import discdogs.composeapp.generated.resources.ic_chevron_left
import discdogs.composeapp.generated.resources.ic_loading
import discdogs.composeapp.generated.resources.ic_share
import discdogs.composeapp.generated.resources.ic_star_empty
import discdogs.composeapp.generated.resources.ic_star_filled
import discdogs.composeapp.generated.resources.ic_stop
import discdogs.composeapp.generated.resources.ic_three_dots
import discdogs.composeapp.generated.resources.ic_vinyl_format
import discdogs.composeapp.generated.resources.icon_apple_music
import discdogs.composeapp.generated.resources.icon_spotify
import discdogs.composeapp.generated.resources.icon_youtube_music
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel
) {
    val moreBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val barcodeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                DetailEffect.DismissMoreBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.hide()
                    }
                }

                DetailEffect.ShowMoreBottomSheet -> {
                    scope.launch {
                        moreBottomSheetState.show()
                    }
                }

                DetailEffect.DismissBarcodeBottomSheet -> {
                    scope.launch {
                        barcodeBottomSheetState.hide()
                    }
                }

                DetailEffect.ShowBarcodeBottomSheet -> {
                    scope.launch {
                        barcodeBottomSheetState.show()
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

    Box {
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
                            viewModel.process(DetailEvent.OnBackClicked)
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
                                                viewModel.process(DetailEvent.OnShowMoreBottomSheet)
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
                                    viewModel.process(DetailEvent.OnPreviewTrack(data))
                                })
                        }
                    }

                    else -> ReleaseDetailViewShimmered()

                }
            }


        }

        PlayPreviewView(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp)
                .padding(bottom = 80.dp),
            data = state.playingItem,
            isLoading = state.isPreviewLoading,
            color = 0xFF1A1A1A.toInt(),
            onStop = {
                viewModel.process(DetailEvent.OnReleaseTrack)
            }
        )

        if (state.moreSheetVisible) {
            ResultDetailMoreBottomSheet(
                sheetState = moreBottomSheetState,
                onDismiss = { viewModel.process(DetailEvent.OnDismissMoreBottomSheet) },
                onShare = {
                    viewModel.process(DetailEvent.OnShare)

                },
                onExternal = { type -> viewModel.process(DetailEvent.OnExternalWebsite(type)) },
                hasBarcode = !state.releaseDetail?.barcode.isNullOrEmpty(),
                onBarcode = {
                    viewModel.process(DetailEvent.OnDismissMoreBottomSheet)
                    viewModel.process(DetailEvent.OnShowBarcodeBottomSheet)
                },
                hasMaster = state.releaseDetail?.masterId != -1,
                onReleases = {
                    viewModel.process(DetailEvent.OnDismissMoreBottomSheet)
                    viewModel.process(DetailEvent.OnOtherReleases)
                },
            )
        }
        if (state.barcodeSheetVisible) {
            BarcodeBottomSheet(
                sheetState = moreBottomSheetState,
                onDismiss = {
                    viewModel.process(DetailEvent.OnDismissBarcodeBottomSheet)
                },
                barcode = state.releaseDetail?.barcode
            )
        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarcodeBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    barcode: String?,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = barcode.orEmpty(),
                style = VETheme.typography.text16TextColor200W500,
            )
            EAN13BarcodeWithLabel(
                content = barcode.orEmpty(),
                modifier = Modifier.size(320.dp, 140.dp)
            )
        }


    }
}

@Composable
private fun TrackItemView(data: TrackListUiModel, isPlaying: Boolean, onClick: () -> Unit) {


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


@Composable
private fun PlayPreviewView(
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

@Composable
fun PagerWithSteppable(
    modifier: Modifier = Modifier,
    pages: List<@Composable () -> Unit>,
    fillParentMaxWidth: Boolean = true,
    itemSpacing: Int = 0,
    contentPadding: Int = 0,
    showIndicator: Boolean = true,
    isAlpha: Boolean = false,
) {

    val listState = rememberLazyListState()

    val currentPage by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .maxByOrNull { item ->
                    val viewportStart = listState.layoutInfo.viewportStartOffset
                    val viewportEnd = listState.layoutInfo.viewportEndOffset
                    val itemStart = item.offset
                    val itemEnd = item.offset + item.size

                    val visibleStart = maxOf(itemStart, viewportStart)
                    val visibleEnd = minOf(itemEnd, viewportEnd)

                    visibleEnd - visibleStart
                }?.index ?: 0
        }
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            userScrollEnabled = true,
            contentPadding = PaddingValues(horizontal = contentPadding.dp)
        ) {

            items(pages.size) { index ->
                val targetAlpha = if (index == currentPage) 1f else 0.4f
                val animatedAlpha by animateFloatAsState(
                    targetValue = targetAlpha,
                    animationSpec = tween(durationMillis = 300)
                )

                val boxModifier = Modifier
                    .fillMaxHeight()
                    .padding(end = itemSpacing.dp)
                    .then(if (fillParentMaxWidth) Modifier.fillParentMaxWidth() else Modifier)
                    .then(if (isAlpha) Modifier.alpha(animatedAlpha) else Modifier)

                Box(
                    modifier = boxModifier
                ) {
                    pages[index]()
                }
            }
        }


        if (showIndicator) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(VETheme.colors.cardBackgroundColor)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "${currentPage + 1}/${pages.size}",
                    style = VETheme.typography.text13TextColor100W500
                )
            }
        }
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
                    text = "release_date",
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
                    text = "genres",
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
                    text = "styles",
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
                    text = "formats",
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
                    text = "disc_count",
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
                    text = if (showMore) "show_less" else "show_more",
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultDetailMoreBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    hasMaster: Boolean = false,
    onReleases: () -> Unit,
    onShare: () -> Unit,
    onExternal: (type: ExternalWebsites) -> Unit,
    hasBarcode: Boolean = false,
    onBarcode: () -> Unit,
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
                        text = "show_barcode",
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
                    text = "share",
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
                        text = "show_other_releases",
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
                    text = "show_on_spotify",
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
                    text = "show_on_apple_music",
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
                    text = "show_on_youtube_music",
                    style = VETheme.typography.text16TextColor200W500,
                )

            }
        }

    }

}

