package com.moon.pharm.prescription.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moon.pharm.prescription.viewmodel.PrescriptionViewModel

@Composable
fun PrescriptionScreen(
    navController: NavController,
    viewModel: PrescriptionViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("처방전을 어떻게 등록할까요?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { /* 추후 구현: 카메라 로직 */ },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("📸 카메라로 촬영")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* 추후 구현: 갤러리 로직 */ },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("🖼️ 앨범에서 선택")
        }
    }
}