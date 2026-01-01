package com.moon.pharm.component_ui.component.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.tertiaryLight

@Composable
fun CircularProgressBar() {
    Column (
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(Modifier.height(32.dp))
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 16.dp),
            color = tertiaryLight
        )
    }
}