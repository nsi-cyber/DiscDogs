package com.discdogs.app

import androidx.compose.ui.window.ComposeUIViewController
import com.discdogs.app.app.App
import com.discdogs.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }