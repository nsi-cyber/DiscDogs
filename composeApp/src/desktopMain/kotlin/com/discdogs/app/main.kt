package com.discdogs.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.discdogs.app.app.App
import com.discdogs.app.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DiscDogs",
        ) {
            App()
        }
    }
}