package com.moon.pharm.profile.screen.medication

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.moon.pharm.component_ui.view.PharmPrimaryTabRow
import com.moon.pharm.component_ui.view.StatusBadge
import com.moon.pharm.domain.model.MealTiming
import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.model.MedicationTimeGroup
import com.moon.pharm.domain.model.MedicationType
import com.moon.pharm.domain.model.RepeatType
import com.moon.pharm.profile.R
import com.moon.pharm.profile.viewmodel.MedicationViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun MedicationScreen(
    navController: NavController? = null, viewModel: MedicationViewModel
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        R.string.consult_category_all,
        R.string.consult_category_prescription,
        R.string.consult_category_general,
        R.string.consult_category_supplements)

    // 더미 데이터
    @SuppressLint("NewApi")
    val medicationGroups = remember {
        listOf(
            MedicationTimeGroup(
                timeLabel = "아침 복용 (오전 09:00)",
                items = listOf(
                    MedicationItem(
                        id = "1",
                        name = "오메가3",
                        dosage = "2알",
                        type = MedicationType.SUPPLEMENT,
                        startDate = LocalDate.now(),
                        endDate = null,
                        noEndDate = true,
                        alarmTime = LocalTime.of(9, 0),
                        mealTiming = MealTiming.AFTER_MEAL,
                        repeatType = RepeatType.DAILY,
                        isTaken = true
                    ),
                    MedicationItem(
                        id = "2",
                        name = "종합 비타민",
                        dosage = "1알",
                        type = MedicationType.SUPPLEMENT,
                        startDate = LocalDate.now(),
                        endDate = null,
                        noEndDate = true,
                        alarmTime = LocalTime.of(9, 0),
                        mealTiming = MealTiming.AFTER_MEAL,
                        repeatType = RepeatType.DAILY,
                        isTaken = true
                    )
                )
            ),
            MedicationTimeGroup(
                timeLabel = "점심 복용 (오후 12:30)",
                items = listOf(
                    MedicationItem(
                        id = "3",
                        name = "타이레놀",
                        dosage = "1정",
                        type = MedicationType.OTC,
                        startDate = LocalDate.now(),
                        endDate = null,
                        noEndDate = false,
                        alarmTime = LocalTime.of(12, 30),
                        mealTiming = MealTiming.AFTER_MEAL,
                        repeatType = RepeatType.DAILY,
                        isTaken = false
                    )
                )
            ),
            MedicationTimeGroup(
                timeLabel = "저녁 복용 (오후 07:00)",
                items = listOf(
                    MedicationItem(
                        id = "4",
                        name = "칼슘 마그네슘",
                        dosage = "2캡슐",
                        type = MedicationType.SUPPLEMENT,
                        startDate = LocalDate.now(),
                        endDate = null,
                        noEndDate = true,
                        alarmTime = LocalTime.of(19, 0),
                        mealTiming = MealTiming.AFTER_MEAL,
                        repeatType = RepeatType.DAILY,
                        isTaken = false
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        PharmPrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs,
            onTabSelected = { index -> selectedTabIndex = index }
        )

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProgressCard(total = 6, completed = 3)
            }

            items(medicationGroups) { group ->
                MedicationGroupItem(group)
            }

            item { 
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressCard(total: Int, completed: Int) {
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
                text = "이번 주 복용 준수율 94%",
                fontSize = 13.sp,
                color = SecondFont
            )
        }
    }
}

@Composable
fun MedicationGroupItem(group: MedicationTimeGroup) {
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
                MedicationCard(item, onTakeClick = {})
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
        colors = CardDefaults.cardColors(containerColor = White),
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

            if (item.isTaken) {
                StatusBadge(
                    text = "복용완료",
                    statusColor = Primary,
                    contentColor = White,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            } else {
                OutlinedButton(
                    onClick = { onTakeClick(item) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, primaryLight),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryLight),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "복용하기",
                        tint = primaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = "복용하기", fontSize = 12.sp)
                }
            }
        }
    }
}
