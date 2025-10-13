package com.discdogs.app.domain

import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.master
import discdog.composeapp.generated.resources.release
import org.jetbrains.compose.resources.StringResource

enum class SearchType(val type: String, val title: StringResource) {
    MASTER("master", Res.string.master),
    RELEASE("release", Res.string.release)
}