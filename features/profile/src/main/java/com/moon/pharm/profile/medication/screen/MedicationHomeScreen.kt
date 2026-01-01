package com.moon.pharm.profile.medication.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import com.moon.pharm.component_ui.component.bar.PharmPrimaryTabRow
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationTimeGroup
import com.moon.pharm.profile.medication.model.MedicationPrimaryTab
import com.moon.pharm.profile.medication.viewmodel.MedicationIntent
import com.moon.pharm.profile.medication.viewmodel.MedicationViewModel
import com.moon.pharm.profile.R

@Composable
fun MedicationScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val groupedList by viewModel.groupedMedications.collectAsState()

    val totalCount = uiState.medicationList.size
    val completedCount = uiState.medicationList.count { it.isTaken }

    MedicationContent(
        selectedTab = uiState.selectedTab,
        currentList = groupedList,
        totalCount = totalCount,
        completedCount = completedCount,
        onTabSelected = { viewModel.onTabSelected(it) },
        onTakeClick = { item -> viewModel.onIntent(MedicationIntent.ToggleTaken(item.id)) }
    )
}

@Composable
fun MedicationContent(
    selectedTab: MedicationPrimaryTab,
    currentList: List<MedicationTimeGroup>,
    totalCount: Int,
    completedCount: Int,
    onTabSelected: (MedicationPrimaryTab) -> Unit,
    onTakeClick: (MedicationItem) -> Unit
){
    val tabTitles = MedicationPrimaryTab.entries.map { it.title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTab.index,
            tabs = tabTitles,
            onTabSelected = { index -> onTabSelected(MedicationPrimaryTab.fromIndex(index)) }
        )

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProgressCard(total = totalCount, completed = completedCount)
            }

            items(items = currentList, key = { it.timeLabel }) { group ->
                MedicationGroupItem(group = group, onTakeClick = onTakeClick)
            }

            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressCard(total: Int, completed: Int) {
    val progressValue = if (total > 0) completed.toFloat() / total.toFloat() else 0f

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘 복용 $completed/${total}회 완료",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { completed.toFloat() / total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Primary,
                trackColor = tertiaryLight,
                gapSize = 0.dp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                // TODO: 실제 데이터 기반 계산으로 변경
                text = "이번 주 복용 준수율 94%",
                fontSize = 13.sp,
                color = SecondFont
            )
        }
    }
}

@Composable
fun MedicationGroupItem(group: MedicationTimeGroup, onTakeClick: (MedicationItem) -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFC107))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = group.timeLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SecondFont
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            group.items.forEach { item ->
                MedicationCard(item = item, onTakeClick = onTakeClick)
            }
        }
    }
}

@Composable
fun MedicationCard(
    item: MedicationItem,
    onTakeClick: (MedicationItem) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = White,
            contentColor = Primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = " · ${item.type.label}",
                        fontSize = 13.sp,
                        color = SecondFont
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.mealTiming.label} · ${item.repeatType.label}",
                    fontSize = 13.sp,
                    color = SecondFont
                )
            }

            OutlinedButton(
                onClick = { onTakeClick(item) },
                shape = RoundedCornerShape(10.dp),
                border = if (item.isTaken) null else BorderStroke(1.dp, primaryLight),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (item.isTaken) Primary else Color.Transparent,
                    contentColor = if (item.isTaken) White else primaryLight
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = if (item.isTaken) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (item.isTaken) White else primaryLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(
                        if (item.isTaken) R.string.medication_take_on
                        else R.string.medication_take_off
                    ),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
