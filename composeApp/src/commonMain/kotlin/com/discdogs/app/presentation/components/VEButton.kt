package com.discdogs.app.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme


@Composable
fun VEButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {

    Button(
        modifier = modifier, content = {
            content()
        }, onClick = {
            onClick()
        },
        shape = RoundedCornerShape(45.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = VETheme.colors.primaryColor500
        )
    )

}


