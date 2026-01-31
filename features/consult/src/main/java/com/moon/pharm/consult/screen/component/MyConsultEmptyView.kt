package com.moon.pharm.consult.screen.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.moon.pharm.consult.R

@Composable
fun MyConsultEmptyView(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.my_consult_empty),
        modifier = modifier,
        color = Color.Gray
    )
}