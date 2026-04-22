package com.moon.pharm.ddi.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.ddi.R
import com.moon.pharm.ddi.mapper.asString
import com.moon.pharm.ddi.mapper.toContainerColor
import com.moon.pharm.ddi.mapper.toPointColor
import com.moon.pharm.ddi.model.DdiResultUiModel
import com.moon.pharm.ddi.model.RiskLevelUi
import com.moon.pharm.ddi.viewmodel.DdiSharedViewModel
import com.moon.pharm.ddi.viewmodel.DdiUiState

@Composable
fun DdiResultRoute(
    viewModel: DdiSharedViewModel,
    onNavigateToConsult: (String, List<String>) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DdiResultScreen(
        uiState = uiState,
        onAnalyzeClick = viewModel::analyzeInteractions,
        onRemoveDrug = viewModel::removeDrug,
        onNavigateToConsult = onNavigateToConsult,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DdiResultScreen(
    uiState: DdiUiState,
    onAnalyzeClick: () -> Unit,
    onRemoveDrug: (String) -> Unit,
    onNavigateToConsult: (String, List<String>) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = PharmTheme.colors.background,
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.ddi_title),
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onBackClick
                )
            )
        },
        bottomBar = {
            if (uiState.result != null) {
                Button(
                    onClick = { onNavigateToConsult(uiState.result.interactionSummary, uiState.selectedDrugs) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharmTheme.colors.primary,
                        contentColor = PharmTheme.colors.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.ddi_action_consult), fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onAnalyzeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    enabled = uiState.selectedDrugs.size >= 2 && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharmTheme.colors.primary,
                        contentColor = PharmTheme.colors.onPrimary,
                        disabledContainerColor = PharmTheme.colors.placeholder
                    )
                ) {
                    Text(stringResource(R.string.ddi_action_analyze), fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.ddi_waiting_drugs),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PharmTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.selectedDrugs.forEach { drug ->
                    InputChip(
                        selected = false,
                        onClick = { onRemoveDrug(drug) },
                        label = { Text(drug) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.ddi_delete),
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = PharmTheme.colors.surface,
                            labelColor = PharmTheme.colors.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                when {
                    uiState.isLoading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 40.dp)) {
                            CircularProgressIndicator(color = PharmTheme.colors.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.ddi_loading_message),
                                color = PharmTheme.colors.secondFont
                            )
                        }
                    }
                    uiState.userMessage != null -> {
                        Text(
                            text = uiState.userMessage.asString(),
                            color = PharmTheme.colors.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    }
                    uiState.result != null -> {
                        DdiResultCard(result = uiState.result)
                    }
                    uiState.selectedDrugs.size < 2 -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 60.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = PharmTheme.colors.placeholder,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.ddi_empty_guide),
                                color = PharmTheme.colors.secondFont,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DdiResultCard(result: DdiResultUiModel) {
    val containerColor = result.riskLevel.toContainerColor()
    val pointColor = result.riskLevel.toPointColor()
    val riskText = result.riskLevel.asString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = pointColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    riskText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = pointColor
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = PharmTheme.colors.onSurface.copy(alpha = 0.1f))

            Text(
                result.interactionSummary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = PharmTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.ddi_detail_warning),
                style = MaterialTheme.typography.labelLarge,
                color = PharmTheme.colors.secondFont
            )
            Spacer(modifier = Modifier.height(4.dp))

            result.details.forEach { detail ->
                Text(
                    "• $detail",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PharmTheme.colors.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun DdiResultScreenPreview() {
    PharmMasterTheme {
        DdiResultScreen(
            uiState = DdiUiState(
                selectedDrugs = listOf("타이레놀8시간이알서방정", "아스피린장용정"),
                isLoading = false,
                result = DdiResultUiModel(
                    riskLevel = RiskLevelUi.HIGH,
                    interactionSummary = "두 약물을 함께 복용 시 위장출혈 위험이 크게 증가할 수 있습니다.",
                    details = listOf(
                        "반드시 식후 30분에 충분한 물과 함께 복용하세요.",
                        "속쓰림, 흑색변 등의 증상이 나타나면 즉시 복용을 중단하고 의사나 약사와 상담하세요."
                    )
                )
            ),
            onAnalyzeClick = {},
            onRemoveDrug = {},
            onNavigateToConsult = { _, _ -> },
            onBackClick = {}
        )
    }
}