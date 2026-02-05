package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.util.clickableSingle
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.consult.mapper.toBackgroundColor
import com.moon.pharm.consult.mapper.toTextColor
import com.moon.pharm.domain.model.consult.ConsultItem

@Composable
fun ConsultItemCard(
    item: ConsultItem,
    currentUserId: String?,
    isPharmacist: Boolean,
    onClick: () -> Unit
) {
    val isSecret = !item.isPublic
    val isOwner = !currentUserId.isNullOrBlank() && item.userId == currentUserId
    val isAssignedPharmacist = isPharmacist && item.pharmacistId == currentUserId
    val hasPermission = !isSecret || isOwner || isAssignedPharmacist

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSecret) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.consult_secret_icon_desc),
                            tint = if (hasPermission) primaryLight else SecondFont,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = if (hasPermission) item.title else stringResource(R.string.consult_secret_hidden_title),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasPermission) primaryLight else SecondFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${item.nickName} • ${item.createdAt.toDisplayDateTimeString()}",
                    fontSize = 12.sp,
                    color = SecondFont
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            StatusBadge(
                text = item.status.label,
                statusColor = item.status.toBackgroundColor(),
                contentColor = item.status.toTextColor()
            )
        }
    }
}