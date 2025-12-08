package com.moon.pharm.consult.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.consult.model.ConsultItem
import com.moon.pharm.consult.model.dummyConsultItems


@Preview(showBackground = true)
@Composable
fun ConsultScreen(
    navController: NavController? = null
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("최신순", "나의 상담")

    // 탭 선택에 따라 리스트 필터링
    val currentList = if (selectedTabIndex == 0) {
        dummyConsultItems
    } else {
        // TODO: '나의 상담' 필터링 로직 구현 (현재는 임시로 빈 리스트 반환)
        // dummyConsultItems.filter { it.isMyConsult }
        emptyList()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        ConsultTab(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs,
            onTabSelected = { index -> selectedTabIndex = index }
        )
        ConsultList(
            currentList = currentList,
            modifier = Modifier.weight(1f)
        )
    }
}

/* 탭 레이아웃 */
@Composable
fun ConsultTab(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    PrimaryTabRow (
        selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                color = primaryLight
            )
        },
        containerColor = backgroundLight,
        contentColor = primaryLight,
        divider = {}
    ){
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                unselectedContentColor = SecondFont
            )
        }
    }
}

@Composable
fun ConsultList(
    currentList: List<ConsultItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
             .fillMaxSize()
             .background(backgroundLight)
    ) {
        items(currentList) { item ->
            ConsultItemCard(item = item)
        }
    }
}

@Composable
fun ConsultItemCard(item: ConsultItem) {
    ElevatedCard(
        onClick = { /* TODO: 상세 화면 이동 */ },
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
                    text = "ooo • ${item.timeAgo}",
                    fontSize = 12.sp,
                    color = SecondFont
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .background(item.status.color, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = item.status.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.status.textColor
                )
            }
        }
    }
}
