package com.discdogs.app.presentation.components.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.discdogs.app.core.presentation.theme.VETheme
import com.discdogs.app.data.database.model.ReleaseList
import discdog.composeapp.generated.resources.Res
import discdog.composeapp.generated.resources.add_to
import discdog.composeapp.generated.resources.add_to_favorites
import discdog.composeapp.generated.resources.create_new_list
import discdog.composeapp.generated.resources.ic_delete
import discdog.composeapp.generated.resources.ic_plus
import discdog.composeapp.generated.resources.remove_from
import discdog.composeapp.generated.resources.remove_from_favorites
import discdog.composeapp.generated.resources.save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveToListBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    lists: List<ReleaseList>?,
    isFavorite: Boolean,
    releaseInLists: Set<Long>,
    onToggleFavorite: () -> Unit,
    onAddToList: (Long) -> Unit,
    onCreateNewList: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.save),
                style = VETheme.typography.text18TextColor200W400,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 8.dp)
            )

            // Favorites option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleFavorite() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(Res.drawable.ic_delete) else painterResource(
                        Res.drawable.ic_plus
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (!isFavorite) VETheme.colors.textColor100 else VETheme.colors.redColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isFavorite) stringResource(Res.string.remove_from_favorites) else stringResource(
                        Res.string.add_to_favorites
                    ),
                    style = VETheme.typography.text16TextColor200W400
                )
            }


            // Lists
            lists?.forEach { list ->
                val isInList = releaseInLists.contains(list.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onAddToList(list.id) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = if (isInList) painterResource(Res.drawable.ic_delete) else painterResource(
                            Res.drawable.ic_plus
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (!isInList) VETheme.colors.textColor100 else VETheme.colors.redColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isInList) stringResource(
                            Res.string.remove_from,
                            list.name
                        ) else stringResource(Res.string.add_to, list.name),
                        style = VETheme.typography.text16TextColor200W400
                    )
                }
            }


            // Create new list
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onCreateNewList() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = VETheme.colors.primaryColor500
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(Res.string.create_new_list),
                    style = VETheme.typography.text16TextColor200W400.copy(color = VETheme.colors.primaryColor500)
                )
            }
        }
    }
}