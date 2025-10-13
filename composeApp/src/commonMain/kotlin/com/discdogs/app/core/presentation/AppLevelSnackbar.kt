package com.discdogs.app.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.managers.SnackbarEvents
import com.discdogs.app.core.managers.SnackbarHostManager
import com.discdogs.app.core.presentation.theme.VETheme
import kotlinx.coroutines.launch


@Composable
fun AppLevelSnackbar(modifier: Modifier) {

    val scope = rememberCoroutineScope()

    val errorSnackbarHostState = remember {
        SnackbarHostState()
    }

    val successSnackbarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(
        flow = SnackbarHostManager.events, errorSnackbarHostState, successSnackbarHostState
    ) { event ->
        when (event) {


            is SnackbarEvents.ErrorSnackbar -> scope.launch {
                errorSnackbarHostState.currentSnackbarData?.dismiss()
                val message = event.errorMessage?.toStringSuspend().orEmpty()
                errorSnackbarHostState.showSnackbar(
                    withDismissAction = true,
                    message = message,
                    duration = SnackbarDuration.Short,
                )

            }


            is SnackbarEvents.SuccessSnackbar -> {
                scope.launch {
                    successSnackbarHostState.currentSnackbarData?.dismiss()
                    val message = event.successMessage?.toStringSuspend().orEmpty()

                    successSnackbarHostState.showSnackbar(
                        withDismissAction = true,
                        message = message,
                        duration = SnackbarDuration.Short,
                    )

                }
            }
        }

    }

    Column(modifier) {

        SnackbarHost(
            hostState = errorSnackbarHostState,
            modifier = Modifier
                .padding(top = 24.dp),
            snackbar = {
                Snackbar(
                    it,
                    containerColor = VETheme.colors.redColor,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            })

        SnackbarHost(
            hostState = successSnackbarHostState,
            modifier = Modifier
                .padding(top = 24.dp),
            snackbar = {
                Snackbar(
                    it,
                    containerColor = VETheme.colors.greenColor,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            })

    }
}