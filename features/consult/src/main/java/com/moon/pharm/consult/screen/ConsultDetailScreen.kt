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
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.component.StatusBadge
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.model.auth.Pharmacist

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
        pharmacist = uiState.answerPharmacist,
        pharmacistImageUrl = uiState.answerPharmacistProfileUrl
    )
}

@Composable
fun ConsultDetailContent(
    item: ConsultItem?,
    pharmacist: Pharmacist?,
    pharmacistImageUrl: String?
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
                if (item.status == ConsultStatus.COMPLETED && item.answer != null
//                    && pharmacist != null
                ) {
                    AnswerSection(
                        pharmacist = pharmacist,
                        pharmacistImageUrl = pharmacistImageUrl,
                        item = item
                    )
                } else if (item.status == ConsultStatus.WAITING) {
                    Spacer(modifier = Modifier.height(20.dp))
                    WaitingForAnswerBox()
                }
            }
        }
    } else {
        CircularProgressBar()
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
                text = "${item.userId} • ${item.createdAt.toDisplayDateTimeString()}",
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
    pharmacist: Pharmacist?,
    pharmacistImageUrl: String?,
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
                if (pharmacist != null) {
                    Image(
                        painter = painterResource(id = pharmacistImageUrl?.toIntOrNull() ?: 0),
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
                } else {
                    Text(
                        text = "약사 정보 없음",
                        fontSize = 13.sp,
                        color = SecondFont
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
                    text = "답변 일시: ${answer.createdAt.toDisplayDateTimeString()}",
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
private fun ImagePlaceholder(imageUrl: String) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
    ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = SecondFont)
    }
}
