package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.domain.model.consult.ConsultItem

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