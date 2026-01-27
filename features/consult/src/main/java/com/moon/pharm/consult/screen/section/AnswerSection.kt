package com.moon.pharm.consult.screen.section

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
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.component.AnswerContentCard
import com.moon.pharm.consult.screen.component.PharmacistProfileCard
import com.moon.pharm.domain.model.auth.Pharmacist
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