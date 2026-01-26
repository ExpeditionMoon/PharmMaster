package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.button.PharmPrimaryButton
import com.moon.pharm.component_ui.component.input.PrimaryTextField
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.consult.R

@Composable
fun PharmacistAnswerInputSection(
    input: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "약사님, 답변을 등록해주세요",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        PrimaryTextField(
            value = input,
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.consult_answer_write_placeholder_content),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = primaryLight,
                lineHeight = 24.sp
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.TopStart
        )

        Spacer(modifier = Modifier.height(16.dp))

        PharmPrimaryButton(
            text = "답변 등록하기",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = input.isNotBlank()
        )
    }
}