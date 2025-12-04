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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.onPrimaryLight
import com.moon.pharm.component_ui.theme.onSecondaryLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryLight

data class ConsultItem(
    val id: String,
    val title: String,
    val timeAgo: String,
    val status: ConsultStatus,
    val isNew: Boolean
)

enum class ConsultStatus(val label: String, val color: Color, val textColor: Color){
    WAITING("답변 대기", secondaryLight, onSecondaryLight),
    COMPLETED("답변 완료", primaryLight, onPrimaryLight)
}

@Preview(showBackground = true)
@Composable
fun ConsultScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("최신순", "나의 상담")

    // 더미 데이터 생성
    val allConsultList = remember {
        listOf(
            ConsultItem("1", "혈압약 복용 시 주의사항이 궁금합니다", "2시간 전", ConsultStatus.COMPLETED, true),
            ConsultItem("2", "감기약 복용 후 졸음 증상", "6시간 전", ConsultStatus.COMPLETED, false),
            ConsultItem("3", "당뇨약과 다른 약물의 상호작용", "4시간 전", ConsultStatus.WAITING, true),
            ConsultItem("4", "항생제 복용 중 음주 가능한지 문의", "8시간 전", ConsultStatus.WAITING, false),
            ConsultItem("5", "소화제 복용법과 효과적인 복용 시간", "12시간 전", ConsultStatus.COMPLETED, true),
            ConsultItem("6", "알레르기 약물과 음식 섭취 관련", "1일 전", ConsultStatus.WAITING, false),
            ConsultItem("7", "비타민 보충제 복용 순서 문의", "1일 전", ConsultStatus.WAITING, true),
            ConsultItem("8", "진통제 장기 복용의 부작용", "2일 전", ConsultStatus.COMPLETED, false),
            ConsultItem("9", "임신 중 복용 가능한 약물 문의", "3일 전", ConsultStatus.WAITING, false),
            ConsultItem("10", "수면제 의존성에 대한 질문", "4일 전", ConsultStatus.COMPLETED, true),
            ConsultItem("11", "혈압약 복용 시 주의사항이 궁금합니다", "5일 전", ConsultStatus.COMPLETED, true),
            ConsultItem("12", "감기약 복용 후 졸음 증상", "6일 전", ConsultStatus.COMPLETED, false),
            ConsultItem("13", "당뇨약과 다른 약물의 상호작용", "6일 전", ConsultStatus.WAITING, true),
        )
    }

    // 탭 선택에 따라 리스트 필터링
    val currentList = if (selectedTabIndex == 0) {
        allConsultList
    } else {
        // TODO: '나의 상담' 필터링 로직 구현 (현재는 임시로 빈 리스트 반환)
        // allConsultList.filter { it.isMyConsult }
        emptyList()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
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
        containerColor = White,
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
        modifier = modifier.fillMaxSize()
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
