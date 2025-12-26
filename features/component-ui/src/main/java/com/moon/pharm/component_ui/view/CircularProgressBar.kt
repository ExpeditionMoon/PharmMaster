package com.moon.pharm.component_ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.tertiaryLight

@Composable
fun CircularProgressIndicatorSample() {
    Column (
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Undefined ProgressBar",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W800)
        )
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 16.dp),
            color = tertiaryLight
        )
    }
}