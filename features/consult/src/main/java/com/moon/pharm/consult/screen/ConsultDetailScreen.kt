package com.moon.pharm.consult.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.consult.R
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.view.StatusBadge
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist

@Composable
fun ConsultDetailScreen(
    consultId: String,
    viewModel: ConsultViewModel
) {
    LaunchedEffect(consultId) {
        viewModel.getConsultDetail(consultId)
    }

    val uiState by viewModel.uiState.collectAsState()

    ConsultDetailContent(
        item = uiState.selectedItem,
        pharmacist = uiState.pharmacist
    )
}

@Composable
fun ConsultDetailContent(
    item: ConsultItem?,
    pharmacist: Pharmacist?
) {
    if (item != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundLight)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            item {
                QuestionSection(item)
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                if (item.status == ConsultStatus.COMPLETED && item.answer != null && pharmacist != null) {
                    AnswerSection(pharmacist = pharmacist, item = item)
                } else if (item.status == ConsultStatus.WAITING) {
                    Spacer(modifier = Modifier.height(20.dp))
                    WaitingForAnswerBox()
                }
            }
        }
    } else {
        // TODO: 로딩 화면
    }
}

@Composable
fun QuestionSection(item: ConsultItem) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.userId} • ${item.createdAt}",
                fontSize = 13.sp,
                color = SecondFont
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
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item.images.forEach { image ->
                    ImagePlaceholder(image.imageName)
                }
            }
        }
    }
}

@Composable
fun AnswerSection(
    pharmacist: Pharmacist,
    item: ConsultItem
) {
    val answer = item.answer ?: return

    Column {
        Text(
            text = stringResource(R.string.consult_detail_answer_section),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = pharmacist.imageUrl.toIntOrNull() ?: 0),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = pharmacist.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = answer.content,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "답변 일시: ${answer.createdAt}",
                    fontSize = 12.sp,
                    color = SecondFont
                )
            }
        }
    }
}

@Composable
fun WaitingForAnswerBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .border(1.dp, primaryLight, RoundedCornerShape(10.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.padding(end = 10.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.consult_status_waiting_title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Text(
                text = stringResource(R.string.consult_status_waiting_subtitle),
                fontSize = 13.sp,
                color = SecondFont,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ImagePlaceholder(imageUrl: String = "") {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
    ){
        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = SecondFont)
    }
}

@Preview(showBackground = true, name = "답변 완료 상태")
@Composable
private fun ConsultDetailScreenAnsweredPreview() {
    val fakeItem = ConsultItem(
        id = "1",
        userId = "사용자A",
        title = "혈압약 복용 질문입니다.",
        content = "혈압약을 아침에 먹어야 하나요, 저녁에 먹어야 하나요? 가끔 까먹고 안 먹을 때가 있는데 어떻게 하죠?",
        status = ConsultStatus.COMPLETED,
        createdAt = "2025-06-21",
        images = emptyList(),
        expertId = "p1",
        answer = com.moon.pharm.domain.model.ConsultAnswer(
            answerId = "a1",
            expertId = "p1",
            content = "안녕하세요. 혈압약은 가급적 일정한 시간에 복용하시는 것이 가장 중요합니다. \n\n보통은 잊지 않기 위해 아침 식사 후 복용을 권장드리지만, 상황에 따라 다를 수 있습니다. \n\n만약 복용을 잊으셨다면 생각난 즉시 복용하시되, 다음 복용 시간이 가깝다면 다음 시간부터 정량 복용하세요.",
            createdAt = "2025-06-22"
        )
    )

    val fakePharmacist = Pharmacist(
        id = "p1",
        name = "김약사",
        imageUrl = "", // 기본 이미지로 대체됨
        bio = "만성질환 전문 상담 약사입니다."
    )

    ConsultDetailContent(
        item = fakeItem,
        pharmacist = fakePharmacist
    )
}

@Preview(showBackground = true, name = "답변 대기 상태")
@Composable
private fun ConsultDetailScreenWaitingPreview() {
    val fakeWaitingItem = ConsultItem(
        id = "2",
        userId = "사용자B",
        title = "비타민 추천 부탁드려요",
        content = "요즘 너무 피곤해서 비타민을 먹어보려 합니다.",
        status = ConsultStatus.WAITING,
        createdAt = "2025-06-21",
        images = emptyList(),
        expertId = "",
        answer = null
    )

    ConsultDetailContent(
        item = fakeWaitingItem,
        pharmacist = null
    )
}
