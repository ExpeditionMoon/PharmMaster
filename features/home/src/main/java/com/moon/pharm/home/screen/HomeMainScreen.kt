package com.moon.pharm.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moon.pharm.designsystem.R
import com.moon.pharm.designsystem.component.SectionHeader
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.component.card.HealthInfoCard
import com.moon.pharm.designsystem.component.fab.PharmPrescriptionFAB
import com.moon.pharm.designsystem.model.TopBarAction
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.home.viewmodel.HomeMedicationReminder
import com.moon.pharm.home.viewmodel.HomeMedicationSummary
import com.moon.pharm.home.viewmodel.HomeViewModel
import java.time.Instant
import java.time.ZoneId

@Composable
fun HomeMainScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToPrescriptionCapture: () -> Unit,
    onNavigateToMedication: () -> Unit,
    onNavigateToMedicationCreate: () -> Unit
) {
    val scrollState = rememberScrollState()
    val nickname by viewModel.nickname.collectAsState()
    val homeState by viewModel.uiState.collectAsState()
    val displayName = nickname.ifEmpty { "회원" }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = "홈",
                    navigationType = TopBarNavigationType.Menu,
                    isLogoTitle = true,
                    actions = listOf(
                        TopBarAction(icon = Icons.Filled.Search, onClick = onNavigateToSearch),
                        TopBarAction(icon = Icons.Filled.Notifications, onClick = {})
                    )
                )
            )
        },
        floatingActionButton = {
            PharmPrescriptionFAB(
                onClick = onNavigateToPrescriptionCapture
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PharmTheme.colors.background)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = "${displayName}님, 건강 챙기세요!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PharmTheme.colors.onSurface
                )

                PharmNotice(
                    reminder = homeState.nextMedicationReminder,
                    remainingReminderCount = homeState.remainingReminderCount,
                    medicationSummary = homeState.medicationSummary,
                    onCardClick = when (homeState.medicationSummary) {
                        HomeMedicationSummary.NoRegisteredMedication -> null
                        HomeMedicationSummary.NoOngoingMedication -> null
                        HomeMedicationSummary.NoTodayMedication,
                        HomeMedicationSummary.PausedMedication,
                        HomeMedicationSummary.MedicationStartsLater,
                        is HomeMedicationSummary.TodayMedication -> onNavigateToMedication
                        HomeMedicationSummary.Loading,
                        HomeMedicationSummary.LoadFailed -> null
                    },
                    onMedicationCreateClick = onNavigateToMedicationCreate,
                    onMoreClick = onNavigateToMedication
                )

                RateOfUse(
                    medicationSummary = homeState.medicationSummary,
                    weeklyAdherencePercent = homeState.weeklyAdherencePercent,
                    weeklyMedicationCount = homeState.weeklyMedicationCount
                )

                PharmSafety()

                HealthInfo()
            }
        }
    }
}

@Composable
fun PharmNotice(
    reminder: HomeMedicationReminder?,
    remainingReminderCount: Int,
    medicationSummary: HomeMedicationSummary,
    onCardClick: (() -> Unit)?,
    onMedicationCreateClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val medicationRegistrationActionText = when (medicationSummary) {
        HomeMedicationSummary.NoRegisteredMedication -> "첫 복약 알림 등록하기 >"
        HomeMedicationSummary.NoOngoingMedication -> "새 복약 알림 등록하기 >"
        else -> null
    }
    val title = when {
        medicationSummary is HomeMedicationSummary.Loading -> "복약 정보를 불러오는 중이에요"
        medicationSummary is HomeMedicationSummary.LoadFailed -> "알림 정보를 불러오지 못했어요"
        reminder != null -> reminder.time.toHomeTimeString()
        medicationSummary is HomeMedicationSummary.NoRegisteredMedication -> "등록된 복약 알림이 없어요"
        medicationSummary is HomeMedicationSummary.NoOngoingMedication -> "진행 중인 복약이 없어요"
        medicationSummary is HomeMedicationSummary.PausedMedication -> "일시 중지된 복약이 있어요"
        medicationSummary is HomeMedicationSummary.MedicationStartsLater -> "복약이 시작되기 전이에요"
        medicationSummary is HomeMedicationSummary.NoTodayMedication -> "오늘 예정된 알림이 없어요"
        else -> "오늘 남은 알림이 없어요"
    }
    val description = when {
        medicationSummary is HomeMedicationSummary.LoadFailed -> "잠시 후 다시 확인해 주세요"
        medicationSummary is HomeMedicationSummary.Loading -> "복약 정보를 확인하고 있어요"
        reminder != null -> "${reminder.displayName} 복용 시간입니다"
        medicationSummary is HomeMedicationSummary.NoRegisteredMedication -> "첫 복약 알림을 등록해 보세요"
        medicationSummary is HomeMedicationSummary.NoOngoingMedication -> "새 복약 알림을 등록해 보세요"
        medicationSummary is HomeMedicationSummary.PausedMedication -> "복약 관리에서 복약을 재개할 수 있어요"
        medicationSummary is HomeMedicationSummary.MedicationStartsLater -> "설정한 시작일 이후에 알림을 받을 수 있어요"
        medicationSummary is HomeMedicationSummary.NoTodayMedication -> "등록한 복약은 복약 관리에서 확인할 수 있어요"
        else -> "오늘 복약 현황은 아래에서 확인할 수 있어요"
    }
    val actionText = when {
        medicationSummary is HomeMedicationSummary.Loading ||
            medicationSummary is HomeMedicationSummary.LoadFailed -> null
        reminder != null -> "${reminder.medicationCount}개 약 · 오늘 남은 ${remainingReminderCount}개 알림"
        medicationSummary is HomeMedicationSummary.NoTodayMedication ||
            medicationSummary is HomeMedicationSummary.PausedMedication ||
            medicationSummary is HomeMedicationSummary.MedicationStartsLater ||
            medicationSummary is HomeMedicationSummary.TodayMedication -> "복약 관리로 이동 >"
        else -> null
    }
    val cardModifier = Modifier
        .fillMaxWidth()
        .height(130.dp)
        .border(
            width = 0.5.dp,
            color = PharmTheme.colors.placeholder,
            shape = RoundedCornerShape(10.dp)
        )
        .background(PharmTheme.colors.surface)
        .let { modifier ->
            onCardClick?.let { onClick -> modifier.clickable(onClick = onClick) } ?: modifier
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        SectionHeader(
            title = "나의 알림",
            onMoreClick = onMoreClick
        )
        Column(
            modifier = cardModifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 10.dp)
                        .background(
                            color = PharmTheme.colors.secondary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(100.dp)
                        )
                ) {
                    Icon(
                        Icons.Filled.Medication,
                        contentDescription = "medication",
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = PharmTheme.colors.secondFont
                    )
                }
            }
            if (medicationRegistrationActionText != null) {
                TextButton(
                    onClick = onMedicationCreateClick,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                ) {
                    Text(text = medicationRegistrationActionText)
                }
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 20.dp),
                    text = actionText ?: "오늘 남은 알림 없음",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = PharmTheme.colors.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun RateOfUse(
    medicationSummary: HomeMedicationSummary,
    weeklyAdherencePercent: Int?,
    weeklyMedicationCount: Int?
) {
    when (medicationSummary) {
        HomeMedicationSummary.Loading -> MedicationSummaryCard(
            title = "오늘 복약 정보를 불러오는 중이에요"
        )
        HomeMedicationSummary.NoRegisteredMedication,
        HomeMedicationSummary.NoOngoingMedication -> Unit
        HomeMedicationSummary.PausedMedication -> MedicationSummaryCard(
            title = "일시 중지된 복약이 있어요",
            description = "복약 관리에서 복약을 재개할 수 있어요."
        )
        HomeMedicationSummary.MedicationStartsLater -> MedicationSummaryCard(
            title = "복약이 시작되기 전이에요",
            description = "설정한 시작일 이후에 오늘 복약 현황을 확인할 수 있어요."
        )
        HomeMedicationSummary.NoTodayMedication -> MedicationSummaryCard(
            title = "오늘 예정된 복약이 없어요"
        )
        is HomeMedicationSummary.TodayMedication -> TodayMedicationSummaryCard(
            totalCount = medicationSummary.totalCount,
            completedCount = medicationSummary.completedCount,
            lastCompletedTime = medicationSummary.lastCompletedTime,
            weeklyAdherencePercent = weeklyAdherencePercent,
            weeklyMedicationCount = weeklyMedicationCount
        )
        HomeMedicationSummary.LoadFailed -> MedicationSummaryCard(
            title = "오늘 복약 정보를 불러오지 못했어요",
            description = "잠시 후 다시 확인해 주세요."
        )
    }
}

@Composable
private fun TodayMedicationSummaryCard(
    totalCount: Int,
    completedCount: Int,
    lastCompletedTime: Long?,
    weeklyAdherencePercent: Int?,
    weeklyMedicationCount: Int?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = PharmTheme.colors.secondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .heightIn(min = 60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .border(
                    width = 1.dp,
                    color = PharmTheme.colors.secondary,
                    shape = RoundedCornerShape(10.dp)
                )
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "이번 주 복용률",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = when {
                    weeklyMedicationCount == null -> "집계 중"
                    weeklyMedicationCount == 0 -> "-"
                    else -> "${weeklyAdherencePercent}%"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PharmTheme.colors.secondFont
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "오늘 복약 $completedCount/$totalCount 완료",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = lastCompletedTime?.toHomeCompletedTimeString()
                    ?.let { "마지막 복약 완료 · $it" }
                    ?: "오늘 복약 기록을 기준으로 표시합니다",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = PharmTheme.colors.secondFont
            )
        }
    }
}

@Composable
private fun MedicationSummaryCard(
    title: String,
    description: String? = null,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = PharmTheme.colors.secondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        description?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = PharmTheme.colors.secondFont
            )
        }
        action?.invoke()
    }
}

private fun String.toHomeTimeString(): String = runCatching {
    val (hourText, minute) = split(":")
    val hour = hourText.toInt()
    val period = if (hour < 12) "오전" else "오후"
    val displayHour = when (hour % 12) {
        0 -> 12
        else -> hour % 12
    }
    "$period $displayHour:$minute"
}.getOrDefault(this)

private fun Long.toHomeCompletedTimeString(): String {
    val time = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    val period = if (time.hour < 12) "오전" else "오후"
    val displayHour = when (time.hour % 12) {
        0 -> 12
        else -> time.hour % 12
    }

    return "$period $displayHour:${time.minute.toString().padStart(2, '0')}"
}

@Composable
fun PharmSafety() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .heightIn(min = 140.dp)
            .background(
                color = PharmTheme.colors.primary,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                Icons.Outlined.VerifiedUser,
                contentDescription = "verifiedUser",
                tint = PharmTheme.colors.surface,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Column(modifier = Modifier.padding(end = 20.dp)) {
                Text(
                    text = "복용 전 약물 안전성을 확인하세요!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PharmTheme.colors.surface
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "복용 중인 약물 간 상호작용(DDI) 위험을 점검하고 건강을 챙기세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = PharmTheme.colors.surface.copy(alpha = 0.8f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 65.dp, end = 20.dp, top = 12.dp, bottom = 16.dp)
                .background(
                    color = PharmTheme.colors.surface,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "안전성 바로 확인하기 >",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = PharmTheme.colors.primary,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { }
            )
        }
    }
}

@Composable
fun HealthInfo() {
    /* 건강 정보 */
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        SectionHeader(
            title = "건강 정보"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HealthInfoCard(
                imageResId = R.drawable.health_info1,
                title = "올바른 약 복용법",
                description = "물과 함께 복용하는 것이 가장 좋습니다."
            )
            HealthInfoCard(
                imageResId = R.drawable.health_info2,
                title = "의약품 보관법",
                description = "의약품은 직사광선이 닿지 않는 서늘하고 ..."
            )
            HealthInfoCard(
                imageResId = R.drawable.health_info3,
                title = "유통기한 확인",
                description = "유통기한이 지난 약은 폐기해야 합니다."
            )
        }
    }
}

@ThemePreviews
@Composable
private fun HomeMainScreenPreview() {
    val displayName = "홍길동"

    PharmMasterTheme {
        Scaffold(
            topBar = {
                PharmTopBar(
                    data = TopBarData(
                        title = "홈",
                        navigationType = TopBarNavigationType.Menu,
                        isLogoTitle = true,
                        actions = listOf(
                            TopBarAction(icon = Icons.Filled.Search, onClick = {}),
                            TopBarAction(icon = Icons.Filled.Notifications, onClick = {})
                        )
                    )
                )
            },
            floatingActionButton = {
                PharmPrescriptionFAB(onClick = {})
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(PharmTheme.colors.background)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                ) {
                    Text(
                        text = "${displayName}님, 건강 챙기세요!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PharmTheme.colors.onSurface
                    )

                    PharmNotice(
                        reminder = null,
                        remainingReminderCount = 0,
                        medicationSummary = HomeMedicationSummary.NoRegisteredMedication,
                        onCardClick = {},
                        onMedicationCreateClick = {},
                        onMoreClick = {}
                    )

                    RateOfUse(
                        medicationSummary = HomeMedicationSummary.NoRegisteredMedication,
                        weeklyAdherencePercent = null,
                        weeklyMedicationCount = null
                    )

                    PharmSafety()

                    HealthInfo()
                }
            }
        }
    }
}
