package com.discdogs.app.presentation.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.EAN13BarcodeWithLabel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    barcode: String?,
) {


    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = VETheme.colors.backgroundColorPrimary
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = barcode.orEmpty(),
                style = VETheme.typography.text16TextColor200W500,
            )
            EAN13BarcodeWithLabel(
                content = barcode.orEmpty(),
                modifier = Modifier.size(320.dp, 140.dp)
            )
        }


    }
}