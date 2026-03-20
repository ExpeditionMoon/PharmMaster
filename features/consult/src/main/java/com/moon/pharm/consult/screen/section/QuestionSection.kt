package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.StatusBadge
import com.moon.pharm.component_ui.component.button.PharmIconButton
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.component.ConsultImageItem
import com.moon.pharm.consult.screen.component.ConsultPreviewData
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun QuestionSection(
    item: ConsultItem,
    currentUserId: String?,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.nickName} • ${item.createdAt.toDisplayDateTimeString()}",
                fontSize = 13.sp,
                color = PharmTheme.colors.secondFont,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            StatusBadge(
                text = item.status.label,
                statusColor = if (item.status == ConsultStatus.WAITING) PharmTheme.colors.secondary else PharmTheme.colors.primary,
                contentColor = PharmTheme.colors.surface
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PharmTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.content,
            fontSize = 15.sp,
            color = PharmTheme.colors.secondFont,
            lineHeight = 20.sp
        )

        if (item.images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(item.images) { image ->
                    ConsultImageItem(imageUrl = image.imageUrl)
                }
            }
        }
        if (currentUserId == item.userId) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PharmIconButton(
                    icon = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.consult_confirm_edit_btn),
                    onClick = onEditClick
                )
                Spacer(modifier = Modifier.width(4.dp))
                PharmIconButton(
                    icon = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.consult_delete_confirm),
                    onClick = onDeleteClick
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun QuestionSectionPreview() {
    val dummyItem = ConsultPreviewData.dummyConsultItems.first()

    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            QuestionSection(
                item = dummyItem,
                currentUserId = dummyItem.userId,
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}