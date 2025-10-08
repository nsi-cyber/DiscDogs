package com.discdogs.app.core.presentation.theme


import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class VEColors(
    backgroundColorPrimary: Color,
    blackColor: Color,
    grayColor: Color,
    whiteColor: Color,
    redColor: Color,
    greenColor: Color,
    primaryColor50: Color,
    primaryColor100: Color,
    primaryColor200: Color,
    primaryColor300: Color,
    primaryColor400: Color,
    primaryColor500: Color,
    primaryColor600: Color,
    primaryColor700: Color,
    primaryColor800: Color,
    primaryColor900: Color,
    primaryColor950: Color,
    neutralColor50: Color,
    neutralColor100: Color,
    neutralColor200: Color,
    neutralColor300: Color,
    neutralColor400: Color,
    neutralColor500: Color,
    neutralColor600: Color,
    neutralColor700: Color,
    neutralColor800: Color,
    neutralColor900: Color,
    neutralColor950: Color,
    successColor50: Color,
    successColor100: Color,
    successColor300: Color,
    warningColor50: Color,
    warningColor100: Color,
    warningColor300: Color,
    dangerColor50: Color,
    dangerColor100: Color,
    dangerColor300: Color,
    backgroundColor50: Color,
    navBarBackgroundColor50: Color,
    logoBackgroundColor: Color,
    textColor50: Color,
    textColor100: Color,
    textColor200: Color,
    textColorClick: Color,
    indicatorColor: Color,
    cardBackgroundColor: Color,
    blueCardBackgroundColor: Color,
    secondaryBackgroundColor: Color,
    secondaryCardBackgroundColor: Color,
    thirdCardBackgroundColor: Color,
    borderColor: Color,
    secondaryBorderColor: Color,
    blueGradientCardBackgroundColor: Color,
    constantTextColor: Color,
    warningCardBackgroundColor: Color,
    secondaryWarningCardBackgroundColor: Color,
    backgroundColor100: Color
) {

    var backgroundColorPrimary by mutableStateOf(backgroundColorPrimary)
        private set
    var blackColor by mutableStateOf(blackColor)
        private set
    var grayColor by mutableStateOf(grayColor)
        private set
    var whiteColor by mutableStateOf(whiteColor)
        private set
    var redColor by mutableStateOf(redColor)
        private set
    var greenColor by mutableStateOf(greenColor)
        private set

    // PRIMARY COLORS
    var primaryColor50 by mutableStateOf(primaryColor50)
        private set
    var primaryColor100 by mutableStateOf(primaryColor100)
        private set
    var primaryColor200 by mutableStateOf(primaryColor200)
        private set
    var primaryColor300 by mutableStateOf(primaryColor300)
        private set
    var primaryColor400 by mutableStateOf(primaryColor400)
        private set
    var primaryColor500 by mutableStateOf(primaryColor500)
        private set
    var primaryColor600 by mutableStateOf(primaryColor600)
        private set
    var primaryColor700 by mutableStateOf(primaryColor700)
        private set
    var primaryColor800 by mutableStateOf(primaryColor800)
        private set
    var primaryColor900 by mutableStateOf(primaryColor900)
        private set
    var primaryColor950 by mutableStateOf(primaryColor950)
        private set

    // NEUTRAL COLORS
    var neutralColor50 by mutableStateOf(neutralColor50)
        private set
    var neutralColor100 by mutableStateOf(neutralColor100)
        private set
    var neutralColor200 by mutableStateOf(neutralColor200)
        private set
    var neutralColor300 by mutableStateOf(neutralColor300)
        private set
    var neutralColor400 by mutableStateOf(neutralColor400)
        private set
    var neutralColor500 by mutableStateOf(neutralColor500)
        private set
    var neutralColor600 by mutableStateOf(neutralColor600)
        private set
    var neutralColor700 by mutableStateOf(neutralColor700)
        private set
    var neutralColor800 by mutableStateOf(neutralColor800)
        private set
    var neutralColor900 by mutableStateOf(neutralColor900)
        private set
    var neutralColor950 by mutableStateOf(neutralColor950)
        private set

    // SUCCESS COLORS
    var successColor50 by mutableStateOf(successColor50)
        private set
    var successColor100 by mutableStateOf(successColor100)
        private set
    var successColor300 by mutableStateOf(successColor300)
        private set

    // WARNING COLORS
    var warningColor50 by mutableStateOf(warningColor50)
        private set
    var warningColor100 by mutableStateOf(warningColor100)
        private set
    var warningColor300 by mutableStateOf(warningColor300)
        private set

    // DANGER COLORS
    var dangerColor50 by mutableStateOf(dangerColor50)
        private set
    var dangerColor100 by mutableStateOf(dangerColor100)
        private set
    var dangerColor300 by mutableStateOf(dangerColor300)
        private set

    // BACKGROUND COLORS
    var backgroundColor50 by mutableStateOf(backgroundColor50)
        private set
    var backgroundColor100 by mutableStateOf(backgroundColor100)
        private set
    var warningCardBackgroundColor by mutableStateOf(warningCardBackgroundColor)
        private set
    var secondaryWarningCardBackgroundColor by mutableStateOf(secondaryWarningCardBackgroundColor)
        private set
    var navBarBackgroundColor50 by mutableStateOf(navBarBackgroundColor50)
        private set
    var logoBackgroundColor by mutableStateOf(logoBackgroundColor)
        private set


    // TEXT COLORS
    var textColor50 by mutableStateOf(textColor50)
        private set
    var textColor100 by mutableStateOf(textColor100)
        private set
    var textColor200 by mutableStateOf(textColor200)
        private set
    var constantTextColor by mutableStateOf(constantTextColor)
        private set
    var textColorClick by mutableStateOf(textColorClick)
        private set
    var indicatorColor by mutableStateOf(indicatorColor)
        private set
    var cardBackgroundColor by mutableStateOf(cardBackgroundColor)
        private set
    var secondaryBackgroundColor by mutableStateOf(secondaryBackgroundColor)
        private set
    var secondaryCardBackgroundColor by mutableStateOf(secondaryCardBackgroundColor)
        private set
    var thirdCardBackgroundColor by mutableStateOf(thirdCardBackgroundColor)
        private set
    var borderColor by mutableStateOf(borderColor)
        private set
    var secondaryBorderColor by mutableStateOf(secondaryBorderColor)
        private set
    var blueCardBackgroundColor by mutableStateOf(blueCardBackgroundColor)
        private set
    var blueGradientCardBackgroundColor by mutableStateOf(blueGradientCardBackgroundColor)


    fun update(other: VEColors) {
        backgroundColorPrimary = other.backgroundColorPrimary
        blackColor = other.blackColor
        grayColor = other.grayColor
        whiteColor = other.whiteColor
        redColor = other.redColor
        greenColor = other.greenColor

        // PRIMARY COLORS
        primaryColor50 = other.primaryColor50
        primaryColor100 = other.primaryColor100
        primaryColor200 = other.primaryColor200
        primaryColor300 = other.primaryColor300
        primaryColor400 = other.primaryColor400
        primaryColor500 = other.primaryColor500
        primaryColor600 = other.primaryColor600
        primaryColor700 = other.primaryColor700
        primaryColor800 = other.primaryColor800
        primaryColor900 = other.primaryColor900
        primaryColor950 = other.primaryColor950

        // NEUTRAL COLORS
        neutralColor50 = other.neutralColor50
        neutralColor100 = other.neutralColor100
        neutralColor200 = other.neutralColor200
        neutralColor300 = other.neutralColor300
        neutralColor400 = other.neutralColor400
        neutralColor500 = other.neutralColor500
        neutralColor600 = other.neutralColor600
        neutralColor700 = other.neutralColor700
        neutralColor800 = other.neutralColor800
        neutralColor900 = other.neutralColor900
        neutralColor950 = other.neutralColor950

        // SUCCESS COLORS
        successColor50 = other.successColor50
        successColor100 = other.successColor100
        successColor300 = other.successColor300

        // WARNING COLORS
        warningColor50 = other.warningColor50
        warningColor100 = other.warningColor100
        warningColor300 = other.warningColor300

        // DANGER COLORS
        dangerColor50 = other.dangerColor50
        dangerColor100 = other.dangerColor100
        dangerColor300 = other.dangerColor300

        // BACKGROUND COLORS
        backgroundColor50 = other.backgroundColor50
        backgroundColor100 = other.backgroundColor100
        navBarBackgroundColor50 = other.navBarBackgroundColor50
        logoBackgroundColor = other.logoBackgroundColor
        blueCardBackgroundColor = other.blueCardBackgroundColor

        // TEXT COLORS
        textColor50 = other.textColor50
        textColor100 = other.textColor100
        textColor200 = other.textColor200
        constantTextColor = other.constantTextColor
        textColorClick = other.textColorClick
        indicatorColor = other.indicatorColor

        // BACKGROUND COLOR
        cardBackgroundColor = other.cardBackgroundColor
        secondaryCardBackgroundColor = other.secondaryCardBackgroundColor
        secondaryBackgroundColor = other.secondaryBackgroundColor
        thirdCardBackgroundColor = other.thirdCardBackgroundColor
        blueGradientCardBackgroundColor = other.blueGradientCardBackgroundColor
        warningCardBackgroundColor = other.warningCardBackgroundColor
        secondaryWarningCardBackgroundColor = other.secondaryWarningCardBackgroundColor

        // BORDER COLOR
        borderColor = other.borderColor
        secondaryBorderColor = other.secondaryBorderColor
    }

    fun copy(): VEColors = VEColors(
        backgroundColorPrimary = backgroundColorPrimary,
        blackColor = blackColor,
        grayColor = grayColor,
        whiteColor = whiteColor,
        redColor = redColor,
        greenColor = greenColor,

        // PRIMARY COLORS
        primaryColor50 = primaryColor50,
        primaryColor100 = primaryColor100,
        primaryColor200 = primaryColor200,
        primaryColor300 = primaryColor300,
        primaryColor400 = primaryColor400,
        primaryColor500 = primaryColor500,
        primaryColor600 = primaryColor600,
        primaryColor700 = primaryColor700,
        primaryColor800 = primaryColor800,
        primaryColor900 = primaryColor900,
        primaryColor950 = primaryColor950,

        // NEUTRAL COLORS
        neutralColor50 = neutralColor50,
        neutralColor100 = neutralColor100,
        neutralColor200 = neutralColor200,
        neutralColor300 = neutralColor300,
        neutralColor400 = neutralColor400,
        neutralColor500 = neutralColor500,
        neutralColor600 = neutralColor600,
        neutralColor700 = neutralColor700,
        neutralColor800 = neutralColor800,
        neutralColor900 = neutralColor900,
        neutralColor950 = neutralColor950,

        // SUCCESS COLORS
        successColor50 = successColor50,
        successColor100 = successColor100,
        successColor300 = successColor300,

        // WARNING COLORS
        warningColor50 = warningColor50,
        warningColor100 = warningColor100,
        warningColor300 = warningColor300,

        // DANGER COLORS
        dangerColor50 = dangerColor50,
        dangerColor100 = dangerColor100,
        dangerColor300 = dangerColor300,

        // BACKGROUND COLORS
        backgroundColor50 = backgroundColor50,
        backgroundColor100 = backgroundColor100,
        navBarBackgroundColor50 = navBarBackgroundColor50,
        logoBackgroundColor = logoBackgroundColor,
        blueCardBackgroundColor = blueCardBackgroundColor,
        warningCardBackgroundColor = warningCardBackgroundColor,
        secondaryWarningCardBackgroundColor = secondaryWarningCardBackgroundColor,

        // TEXT COLORS
        textColor50 = textColor50,
        textColor100 = textColor100,
        textColor200 = textColor200,
        constantTextColor = constantTextColor,
        textColorClick = textColorClick,
        indicatorColor = indicatorColor,

        // BACKGROUND COLOR
        cardBackgroundColor = cardBackgroundColor,
        secondaryCardBackgroundColor = secondaryCardBackgroundColor,
        secondaryBackgroundColor = secondaryBackgroundColor,
        thirdCardBackgroundColor = thirdCardBackgroundColor,
        blueGradientCardBackgroundColor = blueGradientCardBackgroundColor,

        // BORDER COLOR
        borderColor = borderColor,
        secondaryBorderColor = secondaryBorderColor,
    )
}

object VECustomColors {
    val COLOR_BLACK = Color(0xFF000000)
    val COLOR_WHITE = Color(0xFFFFFFFF)
    val COLOR_RED = Color(0xFFFF4B4B) // Spotify'daki kırmızıya daha yakın

    // Spotify Green
    val COLOR_GREEN = Color(0xFF1DB954) // Spotify Green (Main Brand Color)

    // PRIMARY COLORS - Yeşil tonları (Spotify Green tonda)
    val PRIMARY_COLOR_50 = Color(0xFFE8F9EF)
    val PRIMARY_COLOR_100 = Color(0xFFCFF3DE)
    val PRIMARY_COLOR_200 = Color(0xFFAEEFC6)
    val PRIMARY_COLOR_300 = Color(0xFF7BE9A6)
    val PRIMARY_COLOR_400 = Color(0xFF3DDC84)
    val PRIMARY_COLOR_500 = Color(0xFF1DB954) // Spotify Green
    val PRIMARY_COLOR_600 = Color(0xFF1AA34A)
    val PRIMARY_COLOR_700 = Color(0xFF168D40)
    val PRIMARY_COLOR_800 = Color(0xFF117536)
    val PRIMARY_COLOR_900 = Color(0xFF0B5C2B)
    val PRIMARY_COLOR_950 = Color(0xFF063F1D)

    // NEUTRAL COLORS - Spotify’ın gri paleti (karanlık arayüzlerde kullanılır)
    val NEUTRAL_COLOR_50 = Color(0xFFF2F2F2)
    val NEUTRAL_COLOR_100 = Color(0xFFE0E0E0)
    val NEUTRAL_COLOR_200 = Color(0xFFBDBDBD)
    val NEUTRAL_COLOR_300 = Color(0xFF9E9E9E)
    val NEUTRAL_COLOR_400 = Color(0xFF757575)
    val NEUTRAL_COLOR_500 = Color(0xFF616161)
    val NEUTRAL_COLOR_600 = Color(0xFF424242)
    val NEUTRAL_COLOR_700 = Color(0xFF303030)
    val NEUTRAL_COLOR_800 = Color(0xFF212121)
    val NEUTRAL_COLOR_900 = Color(0xFF141414)
    val NEUTRAL_COLOR_950 = Color(0xFF0A0A0A)

    // SUCCESS / WARNING / DANGER - Spotify tarzında pastel tonlar
    val SUCCESS_COLOR_50 = Color(0xFFE8F9EF)
    val SUCCESS_COLOR_100 = Color(0xFFCFF3DE)
    val SUCCESS_COLOR_300 = Color(0xFF1DB954)

    val WARNING_COLOR_50 = Color(0xFFFFF4E5)
    val WARNING_COLOR_100 = Color(0xFFFFD699)
    val WARNING_COLOR_300 = Color(0xFFFFB84D)

    val DANGER_COLOR_50 = Color(0xFFFDECEA)
    val DANGER_COLOR_100 = Color(0xFFFFA29B)
    val DANGER_COLOR_300 = Color(0xFFEB5757)

    // BACKGROUND COLORS
    val BACKGROUND_COLOR_PRIMARY = Color(0xFF121212)
    val BACKGROUND_DARK_COLOR_100 = Color(0xFF121212) // Spotify Dark Mode BG
    val BACKGROUND_DARK_COLOR_50 = Color(0xFF181818)
    val NAV_BAR_BACKGROUND_COLOR_50 = Color(0xFFFFFFFF)
    val NAV_BAR_BACKGROUND_DARK_COLOR_50 = Color(0xFF181818)
    val LOGO_BACKGROUND_COLOR = Color(0xFF1DB954) // Spotify green
    val CARD_BACKGROUND_COLOR = Color(0xFF282828)
    val BLUE_CARD_BACKGROUND_COLOR = Color(0xFF1D3557)
    val BLUE_GRADIENT_CARD_BACKGROUND_COLOR = Color(0xFF457B9D)
    val WARNING_CARD_BACKGROUND_COLOR = Color(0x33FFD699)

    // TEXT COLORS
    val TEXT_COLOR_50 = Color(0xFF000000)
    val GRAY_COLOR = Color(0xFFB3B3B3)
    val TEXT_COLOR_100 = Color(0xFF999999)
    val TEXT_COLOR_200 = Color(0xFFFFFFFF)
    val TEXT_DARK_COLOR_50 = Color(0xFFCCCCCC)
    val TEXT_DARK_COLOR_100 = Color(0xFFB3B3B3)
    val TEXT_DARK_COLOR_200 = Color(0xFFFFFFFF)
    val TEXT_COLOR_CLICK = Color(0xFF1DB954) // Yeşil tıklanabilir yazı rengi
    val BORDER_COLOR = Color(0x33FFFFFF)
}
