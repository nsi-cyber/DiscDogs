package com.discdogs.app.core.presentation.theme


import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class VEShapes(
    val materialShapes: Shapes = Shapes(),
    val defaultExtraSmallShape: CornerBasedShape = RoundedCornerShape(4.dp),
    val defaultSmallShape: CornerBasedShape = RoundedCornerShape(6.dp),
    val defaultMediumShape: CornerBasedShape = RoundedCornerShape(8.dp),
    val defaultLargeShape: CornerBasedShape = RoundedCornerShape(10.dp),
    val defaultTwelveShape: CornerBasedShape = RoundedCornerShape(12.dp),
    val defaultSixteenShape: CornerBasedShape = RoundedCornerShape(16.dp),
    val defaultTwentyShape: CornerBasedShape = RoundedCornerShape(20.dp),
    val defaultHundredShape: CornerBasedShape = RoundedCornerShape(100.dp),
    val defaultBottomThirtyTwoShape: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomEnd = 32.dp,
        bottomStart = 32.dp
    ),
    val defaultTwentyFourShape: CornerBasedShape = RoundedCornerShape(24.dp),
    val defaultThirtyTwoShape: CornerBasedShape = RoundedCornerShape(32.dp),
    val defaultFiftySixShape: CornerBasedShape = RoundedCornerShape(56.dp)
) {
    val Shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(7.dp),
        large = RoundedCornerShape(10.dp)
    )
}


private val appShapes = VEShapes()

fun defaultAppShapes() = appShapes

fun appShapes() = appShapes

