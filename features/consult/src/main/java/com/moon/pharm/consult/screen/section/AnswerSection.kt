package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.component.AnswerContentCard
import com.moon.pharm.consult.screen.component.ConsultPreviewData
import com.moon.pharm.consult.screen.component.PharmacistProfileCard
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultItem

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

        PharmacistProfileCard(
            pharmacist = pharmacist,
            pharmacistImageUrl = pharmacistImageUrl
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnswerContentCard(
            answer = answer
        )
    }
}

@ThemePreviews
@Composable
private fun AnswerSectionPreview() {
    val dummyItemWithAnswer = ConsultPreviewData.dummyConsultItems[1].copy(
        answer = ConsultAnswer(
            answerId = "a1",
            pharmacistId = "p1",
            pharmacistName = "김약사",
            content = "네, 타이레놀 복용하셔도 괜찮습니다.",
            createdAt = System.currentTimeMillis()
        )
    )

    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AnswerSection(
                pharmacist = Pharmacist(userId = "p1", name = "김약사", bio = "상담 가능", placeId = "1", pharmacyName = "달빛약국"),
                pharmacistImageUrl = null,
                item = dummyItemWithAnswer
            )
        }
    }
}