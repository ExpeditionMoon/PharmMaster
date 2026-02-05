package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.consult.ConsultItem

@Composable
fun ConsultList(
    currentList: List<ConsultItem>,
    currentUserId: String?,
    isPharmacist: Boolean,
    onItemClick: (ConsultItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (currentList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundLight),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.consult_list_empty_title),
                    fontSize = 16.sp,
                    color = SecondFont,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.consult_list_empty_subtitle),
                    fontSize = 14.sp,
                    color = SecondFont.copy(alpha = 0.7f)
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .background(backgroundLight)
        ) {
            items(
                items = currentList, key = { it.id }) { item ->
                ConsultItemCard(
                    item = item,
                    currentUserId = currentUserId,
                    isPharmacist = isPharmacist,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}