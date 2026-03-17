package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.StatusBadge
import com.moon.pharm.component_ui.component.item.PharmListItem
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.toBackgroundColor
import com.moon.pharm.consult.mapper.toTextColor
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun ConsultItemCard(
    item: ConsultItem,
    currentUserId: String?,
    isPharmacist: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSecret = !item.isPublic
    val isOwner = !currentUserId.isNullOrBlank() && item.userId == currentUserId
    val isAssignedPharmacist = isPharmacist && item.pharmacistId == currentUserId
    val hasPermission = !isSecret || isOwner || isAssignedPharmacist

    val displayTitle = if (hasPermission) item.title else stringResource(R.string.consult_secret_hidden_title)

    PharmListItem(
        modifier = modifier,
        onClick = onClick,
        leading = if (isSecret) {
            {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(R.string.consult_secret_icon_desc),
                    tint = if (hasPermission) PharmTheme.colors.primary else PharmTheme.colors.placeholder,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else null,
        headline = displayTitle,
        subhead = "${item.nickName} • ${item.createdAt.toDisplayDateTimeString()}",
        trailing = {
            StatusBadge(
                text = item.status.label,
                statusColor = item.status.toBackgroundColor(),
                contentColor = item.status.toTextColor()
            )
        }
    )
}

@ThemePreviews
@Composable
private fun ConsultItemCardPreview() {
    PharmMasterTheme {
        Box(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ConsultItemCard(
                    item = ConsultItem(id = "1", userId = "u1", pharmacistId = "p1", nickName = "사용자", title = "일반 공개 상담입니다.", content = "...", isPublic = true, status = ConsultStatus.WAITING, createdAt = System.currentTimeMillis()),
                    currentUserId = "u1",
                    isPharmacist = false,
                    onClick = {}
                )

                ConsultItemCard(
                    item = ConsultItem(id = "2", userId = "u2", pharmacistId = "p1", nickName = "익명", title = "비밀글입니다", content = "...", isPublic = false, status = ConsultStatus.COMPLETED, createdAt = System.currentTimeMillis()),
                    currentUserId = "u1", // 권한 없는 유저
                    isPharmacist = false,
                    onClick = {}
                )
            }
        }
    }
}