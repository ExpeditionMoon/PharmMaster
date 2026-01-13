package com.moon.pharm.consult.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.component_ui.navigation.ContentNavigationRoute
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryContainerLight
import com.moon.pharm.component_ui.theme.secondaryLight
import com.moon.pharm.consult.viewmodel.ConsultViewModel

@Composable
fun ConsultConfirmScreen(
    navController: NavController,
    viewModel: ConsultViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val writeState = uiState.writeState

    LaunchedEffect(uiState.isConsultCreated) {
        if (uiState.isConsultCreated) {
            viewModel.resetConsultState()
        }
    }

    ConsultConfirmContent(
        title = writeState.title,
        content = writeState.content,
        selectedPharmacistName = uiState.answerPharmacist?.name ?: "선택된 약사 없음",
        onEditTitleOrContent = {
            navController.popBackStack(ContentNavigationRoute.ConsultTabWriteScreen, false)
        },
        onEditPharmacist = {
            navController.popBackStack(ContentNavigationRoute.ConsultTabPharmacistScreen, false)
        }
    )
}

@Composable
fun ConsultConfirmContent(
    title: String,
    content: String,
    selectedPharmacistName: String,
    onEditTitleOrContent: () -> Unit,
    onEditPharmacist: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = "약사 최종 검토 및 요청",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryLight,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(secondaryContainerLight, RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoCard(
                label = "제목",
                content = title.ifBlank { "아직 작성되지 않음" },
                onEditClick = onEditTitleOrContent
            )
            InfoCard(
                label = "상담 내용",
                content = content.ifBlank { "아직 작성되지 않음" },
                onEditClick = onEditTitleOrContent
            )
            InfoCard(
                label = "선택된 약사",
                content = selectedPharmacistName,
                onEditClick = onEditPharmacist
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        GuidanceBox(
            text = "상담 요청 전에 모든 내용을 다시 한번 확인해주세요. 요청이 완료되면 약사에게 실시간으로 연결됩니다."
        )
    }
}

@Composable
fun InfoCard(label: String, content: String, onEditClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = SecondFont,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = content,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryLight
                )
            }
            
            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = secondaryLight,
                    contentColor = White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(width = 56.dp, height = 30.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Text(text = "수정", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun GuidanceBox(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, primaryLight, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = primaryLight,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = primaryLight,
            lineHeight = 20.sp
        )
    }
}
