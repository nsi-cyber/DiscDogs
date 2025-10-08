package com.discdogs.app.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class VETypography(
    val materialTypography: Typography,
) {

    val text14W400: TextStyle
        @Composable
        get() = TextStyle(fontSize = 14.sp)

    val text14TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            color = VETheme.colors.textColor200,
        )


    val text26TextColor200W600: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            color = VETheme.colors.whiteColor,
            fontSize = 26.sp
        )
    val text34TextColor200W600: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            color = VETheme.colors.whiteColor,
            fontSize = 34.sp
        )

    val text13TextColor100W500: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            color = VETheme.colors.textColor100,
            fontSize = 13.sp
        )

    val text14TextColor200W600: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            color = VETheme.colors.textColor200,
        )

    val text14TextColor100W600: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            color = VETheme.colors.textColor100,
        )

    val text14TextColor100W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,

            color = VETheme.colors.textColor100,
        )

    val text14PrimaryColor600W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = VETheme.colors.primaryColor600,
        )

    val text14TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,

            color = VETheme.colors.textColor200,
        )
    val text16TextColor600W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,

            color = VETheme.colors.primaryColor600,
        )

    val text18TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,

            color = VETheme.colors.textColor200,
        )

    val text18TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,

            color = VETheme.colors.textColor200,
        )

    val text12TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,

            color = VETheme.colors.textColor200,
        )
    val text12TextColor100W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,

            color = VETheme.colors.textColor100,
        )
    val text14TextColor50W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,

            color = VETheme.colors.textColor50,
        )
    val text13W400: TextStyle
        @Composable
        get() = TextStyle(fontSize = 13.sp)

    val text13TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = VETheme.colors.textColor200,
        )


    val text13TextColor100W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,

            color = VETheme.colors.textColor100,
        )
    val text13TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,

            color = VETheme.colors.textColor200,
        )
    val text24TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,

            color = VETheme.colors.textColor200,
        )
    val text13TextColor50W400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,

            color = VETheme.colors.textColor50,
        )

    val text16TextColorWhiteW500: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = VETheme.colors.whiteColor,
        )
    val text14TextColorWhiteW400: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = VETheme.colors.whiteColor,
        )

    val text16TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            color = VETheme.colors.textColor200,
        )

    val text16TextColor200W700: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            color = VETheme.colors.textColor200,
            fontWeight = FontWeight.Bold
        )

    val text16TextColor50W700: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            color = VETheme.colors.textColor50,
            fontWeight = FontWeight.Bold
        )

    val text16TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = VETheme.colors.textColor200,
        )
    val clickText16TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            color = VETheme.colors.textColorClick,
        )

    val clickText16TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = VETheme.colors.textColorClick,
        )


    val text34TextColor200W700: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,

            fontSize = 34.sp,
            color = VETheme.colors.textColor200,
        )

    val text16TextColor50W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            color = VETheme.colors.textColor50,
        )
    val text12TextColor50W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 12.sp,
            color = VETheme.colors.textColor50,
        )

    val text20TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 20.sp,
            color = VETheme.colors.textColor200,
        )



    val text20TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = VETheme.colors.textColor200,
        )

    val text20TextColor100W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 20.sp,
            color = VETheme.colors.textColor100,
        )

    val text21TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 21.sp,
            color = VETheme.colors.textColor200,
        )

    val text21TextColor200W500: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium,
            color = VETheme.colors.textColor200,
        )

    val text11TextColor200W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 11.sp,
            color = VETheme.colors.textColor200,
        )

    val text11PrimaryColor600W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 11.sp,
            color = VETheme.colors.primaryColor600,
        )
    val text10PrimaryColor600W400: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 10.sp,
            color = VETheme.colors.primaryColor600,
        )


    val text12TextColor200W300: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 12.sp,
            color = VETheme.colors.textColor200,
            fontWeight = FontWeight.Light
        )


}

fun defaultAppTypography() = appTypography()

fun appTypography(): VETypography {
    val baseTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 22.sp,
        letterSpacing = (0.3f).sp
    )
    return VETypography(
        materialTypography = Typography(bodyLarge = baseTextStyle)
    )
}




@Composable
fun autoSizeNacelle(
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color? = null,
    letterSpacing: TextUnit = (0.3f).sp
): TextStyle {

    return TextStyle(
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        color = color ?: Color.Unspecified
    )
}


