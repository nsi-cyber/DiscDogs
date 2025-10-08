package com.discdogs.app.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.discdogs.app.core.presentation.theme.VETheme.shapes
import com.discdogs.app.core.presentation.theme.VETheme.typography

private val VEColorPalette = VEColors(
    backgroundColorPrimary = VECustomColors.BACKGROUND_COLOR_PRIMARY,
    blackColor = VECustomColors.COLOR_BLACK,
    grayColor = VECustomColors.GRAY_COLOR,
    whiteColor = VECustomColors.COLOR_WHITE,
    redColor = VECustomColors.COLOR_RED,
    greenColor = VECustomColors.COLOR_GREEN,
    primaryColor50 = VECustomColors.PRIMARY_COLOR_50,
    primaryColor100 = VECustomColors.PRIMARY_COLOR_100,
    primaryColor200 = VECustomColors.PRIMARY_COLOR_200,
    primaryColor300 = VECustomColors.PRIMARY_COLOR_300,
    primaryColor400 = VECustomColors.PRIMARY_COLOR_400,
    primaryColor500 = VECustomColors.PRIMARY_COLOR_500,
    primaryColor600 = VECustomColors.PRIMARY_COLOR_600,
    primaryColor700 = VECustomColors.PRIMARY_COLOR_700,
    primaryColor800 = VECustomColors.PRIMARY_COLOR_800,
    primaryColor900 = VECustomColors.PRIMARY_COLOR_900,
    primaryColor950 = VECustomColors.PRIMARY_COLOR_950,
    neutralColor50 = VECustomColors.NEUTRAL_COLOR_50,
    neutralColor100 = VECustomColors.NEUTRAL_COLOR_100,
    neutralColor200 = VECustomColors.NEUTRAL_COLOR_200,
    neutralColor300 = VECustomColors.NEUTRAL_COLOR_300,
    neutralColor400 = VECustomColors.NEUTRAL_COLOR_400,
    neutralColor500 = VECustomColors.NEUTRAL_COLOR_500,
    neutralColor600 = VECustomColors.NEUTRAL_COLOR_600,
    neutralColor700 = VECustomColors.NEUTRAL_COLOR_700,
    neutralColor800 = VECustomColors.NEUTRAL_COLOR_800,
    neutralColor900 = VECustomColors.NEUTRAL_COLOR_900,
    neutralColor950 = VECustomColors.NEUTRAL_COLOR_950,
    successColor50 = VECustomColors.SUCCESS_COLOR_50,
    successColor100 = VECustomColors.SUCCESS_COLOR_100,
    successColor300 = VECustomColors.SUCCESS_COLOR_300,
    warningColor50 = VECustomColors.WARNING_COLOR_50,
    warningColor100 = VECustomColors.WARNING_COLOR_100,
    warningColor300 = VECustomColors.WARNING_COLOR_300,
    dangerColor50 = VECustomColors.DANGER_COLOR_50,
    dangerColor100 = VECustomColors.DANGER_COLOR_100,
    dangerColor300 = VECustomColors.DANGER_COLOR_300,
    backgroundColor50 = VECustomColors.PRIMARY_COLOR_100,
    backgroundColor100 = VECustomColors.COLOR_WHITE,
    navBarBackgroundColor50 = VECustomColors.NAV_BAR_BACKGROUND_COLOR_50,
    logoBackgroundColor = VECustomColors.LOGO_BACKGROUND_COLOR,
    textColor50 = VECustomColors.TEXT_COLOR_50,
    textColor100 = VECustomColors.TEXT_COLOR_100,
    textColor200 = VECustomColors.TEXT_COLOR_200,
    constantTextColor = VECustomColors.TEXT_COLOR_200,
    textColorClick = VECustomColors.TEXT_COLOR_CLICK,
    indicatorColor = VECustomColors.COLOR_BLACK,
    cardBackgroundColor = VECustomColors.CARD_BACKGROUND_COLOR,
    blueCardBackgroundColor = VECustomColors.BLUE_CARD_BACKGROUND_COLOR,
    secondaryCardBackgroundColor = VECustomColors.COLOR_WHITE,
    secondaryBackgroundColor = VECustomColors.COLOR_WHITE,
    thirdCardBackgroundColor = VECustomColors.BORDER_COLOR,
    borderColor = VECustomColors.BORDER_COLOR,
    secondaryBorderColor = VECustomColors.NEUTRAL_COLOR_100,
    blueGradientCardBackgroundColor = VECustomColors.BLUE_GRADIENT_CARD_BACKGROUND_COLOR,
    warningCardBackgroundColor = VECustomColors.WARNING_CARD_BACKGROUND_COLOR,
    secondaryWarningCardBackgroundColor = VECustomColors.PRIMARY_COLOR_100,
)


@Composable
fun VETheme(
    content: @Composable() () -> Unit
) {

    val colors = VEColorPalette

    ProvideVEColors(colors) {
        MaterialTheme(
            typography = typography.materialTypography,
            shapes = shapes.materialShapes,
            content = content
        )
    }
}

object VETheme {
    val colors: VEColors
        @Composable
        @ReadOnlyComposable
        get() = LocalVEColors.current


    val shapes: VEShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current

    val typography: VETypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}

private val LocalAppShapes = staticCompositionLocalOf {
    defaultAppShapes()
}

private val LocalAppTypography = staticCompositionLocalOf {
    defaultAppTypography()
}

private val LocalVEColors = staticCompositionLocalOf<VEColors> {
    VEColorPalette
}

@Composable
fun ProvideVEColors(
    colors: VEColors,
    typography: VETypography = appTypography(),
    shapes: VEShapes = appShapes(),
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(
        LocalVEColors provides colorPalette,
        LocalAppShapes provides shapes,
        LocalAppTypography provides typography,
        content = content,
    )
}