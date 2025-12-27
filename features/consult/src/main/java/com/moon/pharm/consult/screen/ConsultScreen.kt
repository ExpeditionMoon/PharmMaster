package com.moon.pharm.consult.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.view.CircularProgressBar
import com.moon.pharm.component_ui.view.PharmPrimaryTabRow
import com.moon.pharm.component_ui.view.StatusBadge
import com.moon.pharm.consult.mapper.toBackgroundColor
import com.moon.pharm.consult.mapper.toTextColor
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.ConsultItem

@Composable
fun ConsultScreen(
    navController: NavController? = null, viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    Column {
/*        Button(
            onClick = { viewModel.testFirestore() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Firestore 연결 테스트")
        }*/

        ConsultContent(
            selectedTab = uiState.selectedTab,
            currentList = if (uiState.selectedTab == ConsultPrimaryTab.LATEST) uiState.consultList else emptyList(),
            onTabSelected = { viewModel.onTabSelected(it) },
            onItemClick = { id ->
                navController?.navigate(ContentNavigationRoute.ConsultTabDetailScreen(id = id))
            })

        if (uiState.isLoading) {
            CircularProgressBar()
        }
    }
}

@Composable
fun ConsultContent(
    selectedTab: ConsultPrimaryTab,
    currentList: List<ConsultItem>,
    onTabSelected: (ConsultPrimaryTab) -> Unit,
    onItemClick: (String) -> Unit
) {
    val tabTitles = ConsultPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTab.index,
            tabs = tabTitles,
            onTabSelected = { index -> onTabSelected(ConsultPrimaryTab.fromIndex(index)) })

        ConsultList(
            currentList = currentList, onItemClick = onItemClick, modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ConsultList(
    currentList: List<ConsultItem>, onItemClick: (String) -> Unit, modifier: Modifier = Modifier
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
                    text = "등록된 상담 게시글이 없습니다.",
                    fontSize = 16.sp,
                    color = SecondFont,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "첫 번째 상담을 작성해보세요!",
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
                    item = item, onClick = { onItemClick(item.id) })
            }
        }
    }
}

@Composable
fun ConsultItemCard(item: ConsultItem, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${item.userId} • ${item.createdAt}",
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