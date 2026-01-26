package com.moon.pharm.consult.screen.component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.consult.ConsultAnswer

@Composable
fun AnswerContentCard(
    answer: ConsultAnswer
) {
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
                text = stringResource(
                    R.string.consult_detail_answer_date_format,
                    answer.createdAt.toDisplayDateTimeString()
                ),
                fontSize = 12.sp,
                color = SecondFont
            )
        }
    }
}
