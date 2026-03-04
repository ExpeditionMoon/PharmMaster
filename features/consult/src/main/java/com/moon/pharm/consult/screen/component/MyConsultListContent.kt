package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun MyConsultListContent(
    items: List<ConsultItem>,
    currentUserId: String?,
    isPharmacist: Boolean,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = items, key = { it.id }) { item ->
            ConsultItemCard(
                item = item,
                currentUserId = currentUserId,
                isPharmacist = isPharmacist,
                onClick = { onItemClick(item.id) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MyConsultListContentPreview() {
    PharmMasterTheme {
        MyConsultListContent(
            items = listOf(
                ConsultItem(
                    id = "1",
                    userId = "u1",
                    pharmacistId = "p1",
                    nickName = "사용자1",
                    title = "타이레놀 복용 문의",
                    content = "...",
                    isPublic = true,
                    status = ConsultStatus.WAITING,
                    createdAt = System.currentTimeMillis()
                )
            ),
            currentUserId = "u1",
            isPharmacist = false,
            onItemClick = {}
        )
    }
}