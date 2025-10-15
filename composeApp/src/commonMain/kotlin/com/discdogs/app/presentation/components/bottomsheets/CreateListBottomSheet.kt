package com.discdogs.app.presentation.components.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.presentation.components.VEButton
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.create
import discdog.composeapp.generated.resources.create_list_placeholder
import discdog.composeapp.generated.resources.create_list_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var listName by remember { mutableStateOf("") }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = VETheme.colors.backgroundColorPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.create_list_title),
                style = VETheme.typography.text18TextColor200W400,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = VETheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {


                BasicTextField(
                    value = listName,
                    onValueChange = { listName = it },
                    singleLine = true,
                    textStyle = VETheme.typography.text14TextColor200W500,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        if (listName.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.create_list_placeholder),
                                style = VETheme.typography.text14TextColor100W400
                            )
                        }
                        innerTextField()
                    }
                )


            }


            VEButton(
                onClick = {
                    if (listName.isNotBlank()) {
                        onConfirm(listName)
                        listName = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(13.dp),
                    text = stringResource(Res.string.create),
                    style = VETheme.typography.text16TextColor200W700,
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}
