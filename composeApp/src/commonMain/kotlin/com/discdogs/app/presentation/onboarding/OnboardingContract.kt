package com.discdogs.app.presentation.onboarding

import com.discdogs.app.core.presentation.UiText
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.image_onboarding_1
import discdog.composeapp.generated.resources.image_onboarding_2
import discdog.composeapp.generated.resources.image_onboarding_3
import discdog.composeapp.generated.resources.onboarding_first_button
import discdog.composeapp.generated.resources.onboarding_first_desc
import discdog.composeapp.generated.resources.onboarding_first_title
import discdog.composeapp.generated.resources.onboarding_second_button
import discdog.composeapp.generated.resources.onboarding_second_desc
import discdog.composeapp.generated.resources.onboarding_second_title
import discdog.composeapp.generated.resources.onboarding_third_button
import discdog.composeapp.generated.resources.onboarding_third_desc
import discdog.composeapp.generated.resources.onboarding_third_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi


data class OnboardingState @OptIn(InternalResourceApi::class) constructor(
    val currentPageIndex: Int = 0,
    val pages: List<OnboardingPageUiModel> = listOf(
        OnboardingPageUiModel(
            title = UiText.StringResourceId(Res.string.onboarding_first_title),
            desc = UiText.StringResourceId(Res.string.onboarding_first_desc),
            button = UiText.StringResourceId(Res.string.onboarding_first_button),
            image = Res.drawable.image_onboarding_1,
        ),
        OnboardingPageUiModel(
            title = UiText.StringResourceId(Res.string.onboarding_second_title),
            desc = UiText.StringResourceId(Res.string.onboarding_second_desc),
            button = UiText.StringResourceId(Res.string.onboarding_second_button),
            image = Res.drawable.image_onboarding_2,
        ),
        OnboardingPageUiModel(
            title = UiText.StringResourceId(Res.string.onboarding_third_title),
            desc = UiText.StringResourceId(Res.string.onboarding_third_desc),
            button = UiText.StringResourceId(Res.string.onboarding_third_button),
            image = Res.drawable.image_onboarding_3,
        ),
    )

) {
    val currentPage: OnboardingPageUiModel
        get() = pages[currentPageIndex]

}

data class OnboardingPageUiModel(
    val title: UiText,
    val desc: UiText,
    val button: UiText,
    val image: DrawableResource,
)

sealed interface OnboardingEffect {
    data class OnPageChanged(val index: Int) : OnboardingEffect
}

sealed class OnboardingEvent {
    data class OnPageChange(val index: Int) : OnboardingEvent()
}
