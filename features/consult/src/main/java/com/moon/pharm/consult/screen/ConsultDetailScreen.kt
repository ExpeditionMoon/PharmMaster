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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.view.StatusBadge
import com.moon.pharm.consult.model.ConsultItem
import com.moon.pharm.consult.model.ConsultStatus
import com.moon.pharm.consult.model.Pharmacist
import com.moon.pharm.consult.model.dummyPharmacists
import com.moon.pharm.consult.model.getDummyItem

@Composable
fun ConsultDetailScreen(consultId: String) {
    val item = remember { getDummyItem(consultId) }

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
            if (item.status == ConsultStatus.COMPLETED) {
                AnswerSection(pharmacist = dummyPharmacists.first())
            } else {
                WaitingForAnswerBox()
            }
        }
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
                text = "${item.author} • ${item.timeAgo}",
                fontSize = 13.sp,
                color = SecondFont
            )
            StatusBadge(
                text = item.status.label,
                statusColor = item.status.color,
                contentColor = item.status.textColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.content,
            fontSize = 15.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ImagePlaceholder()
            ImagePlaceholder()
        }
    }
}

@Composable
fun AnswerSection(pharmacist: Pharmacist) {
    Column {
        Text(
            text = "약사 답변",
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
                    painter = painterResource(id = pharmacist.imageResId),
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
                    Text(
                        text = pharmacist.specialty,
                        color = SecondFont,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "안녕하세요. 혈압약 복용 시 주의사항에 대 해 자세히 설명드리겠습니다.",
                    fontSize = 14.sp,
                    color = SecondFont,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnswerDetailItem(
                    title = "음식 관련 주의사항",
                    content = "• 자몽이나 자몽주스는 피해주세요. 혈압약 의 효과를 증강시킬 수 있습니다. \n" +
                            "• 과도한 염분 섭취는 혈압 상승의 원인이 되니 저염식을 권합니다. \n" +
                            "• 알코올은 혈압약의 효과를 방해할 수 있 어 제한하시는 것이 좋습니다."
                )
                AnswerDetailItem(
                    title = "다른 약물과의 병용",
                    content = "감감기약 중 일부 성분(슈도에페드린 등)은 혈 압을 상승시킬 수 있으니, 복용 전 반드시 약사나 의사와 상담하시기 바랍니다."
                )
                AnswerDetailItem(
                    title = "생활 습관",
                    content = "규칙적인 운동과 충분한 수면, 스트레스 관 리가 중요합니다. 약물 복용 시간도 일정하 게 유지해주세요."
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
                text = "약사님의 답변을 기다리고 있어요.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Text(
                text = "답변이 등록되면 알림을 통해 알려드리겠습니다.",
                fontSize = 13.sp,
                color = SecondFont,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ImagePlaceholder() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
    )
}

@Composable
private fun AnswerDetailItem(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(text = "• $title", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = content, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 22.sp)
    }
}

@Preview(showBackground = true, name = "답변 완료 상태")
@Composable
private fun ConsultDetailScreenAnsweredPreview() {
    ConsultDetailScreen(consultId = "1")
}

@Preview(showBackground = true, name = "답변 대기 상태")
@Composable
private fun ConsultDetailScreenWaitingPreview() {
    ConsultDetailScreen(consultId = "3")
}
