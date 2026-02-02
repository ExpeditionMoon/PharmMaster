package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.section.AnswerSection
import com.moon.pharm.consult.screen.section.PharmacistAnswerInputSection
import com.moon.pharm.consult.screen.section.QuestionSection
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun ConsultDetailContent(
    isLoading: Boolean,
    item: ConsultItem?,
    pharmacist: Pharmacist?,
    pharmacistImageUrl: String?,

    isPharmacistMode: Boolean = false,
    answerInput: String = "",
    onAnswerChange: (String) -> Unit = {},
    onSubmitAnswer: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
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
                    if (item.status == ConsultStatus.COMPLETED && item.answer != null) {
                        AnswerSection(
                            pharmacist = pharmacist,
                            pharmacistImageUrl = pharmacistImageUrl,
                            item = item
                        )
                    } else if (item.status == ConsultStatus.WAITING) {
                        Spacer(modifier = Modifier.height(20.dp))
                        if (isPharmacistMode) {
                            PharmacistAnswerInputSection(
                                input = answerInput,
                                onValueChange = onAnswerChange,
                                onSubmit = onSubmitAnswer
                            )
                        } else {
                            WaitingForAnswerBox()
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(50.dp)) }
            }
        } else if (!isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.consult_detail_error_load),
                    color = SecondFont,
                    fontSize = 16.sp
                )
            }
        }

        if (isLoading) {
            CircularProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }
}
