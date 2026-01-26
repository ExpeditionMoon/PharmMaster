package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.StatusBadge
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.screen.component.ConsultImageItem
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun QuestionSection(item: ConsultItem) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.nickName} • ${item.createdAt.toDisplayDateTimeString()}",
                fontSize = 13.sp,
                color = SecondFont,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            StatusBadge(
                text = item.status.label,
                statusColor = if (item.status == ConsultStatus.WAITING) Secondary else Primary,
                contentColor = White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.content,
            fontSize = 15.sp,
            color = SecondFont,
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
    }
}