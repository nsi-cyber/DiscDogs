package com.discdogs.app.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme

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
