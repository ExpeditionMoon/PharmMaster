package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.consult.ConsultAnswer

@Composable
fun AnswerContentCard(
    answer: ConsultAnswer
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = PharmTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = answer.content,
                fontSize = 14.sp,
                color = PharmTheme.colors.onSurface,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(
                    R.string.consult_detail_answer_date_format,
                    answer.createdAt.toDisplayDateTimeString()
                ),
                fontSize = 12.sp,
                color = PharmTheme.colors.secondFont
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AnswerContentCardPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AnswerContentCard(
                answer = ConsultAnswer(
                    answerId = "a1",
                    pharmacistId = "p1",
                    pharmacistName = "김약사",
                    content = "식후 30분에 미지근한 물과 함께 복용하시는 것을 권장합니다. 위장 장애가 있을 수 있으니 빈속에는 피해주세요.",
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}