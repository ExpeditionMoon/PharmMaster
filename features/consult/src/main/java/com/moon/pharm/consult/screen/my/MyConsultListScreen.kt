package com.moon.pharm.consult.screen.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.common.asString
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.consult.screen.component.ConsultItemCard
import com.moon.pharm.consult.viewmodel.MyConsultListUiState
import com.moon.pharm.consult.viewmodel.MyConsultListViewModel
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@Composable
fun MyConsultListRoute(
    onNavigateUp: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: MyConsultListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = (userMessage as? ConsultUiMessage)?.asString()

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    MyConsultListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateUp = onNavigateUp,
        onItemClick = onNavigateToDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConsultListScreen(
    uiState: MyConsultListUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = "나의 상담 내역",
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = onNavigateUp
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressBar(modifier = Modifier.align(Alignment.Center))
                }
                uiState.myConsults.isEmpty() -> {
                    Text(
                        text = "작성한 상담 내역이 없습니다.",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.myConsults) { item ->
                            ConsultItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun createMockConsultItems(): List<ConsultItem> {
    val now = System.currentTimeMillis()
    val yesterday = now - (24 * 60 * 60 * 1000)

    return listOf(
        ConsultItem(
            id = "1",
            userId = "user_1",
            nickName = "김약과",
            title = "감기약이랑 비타민 같이 먹어도 되나요?",
            content = "종합감기약 복용 중인데 비타민C를 같이 먹어도 되는지 궁금합니다.",
            status = ConsultStatus.WAITING,
            isPublic = true,
            images = emptyList(),
            createdAt = now,
            answer = null
        ),
        ConsultItem(
            id = "2",
            userId = "user_1",
            nickName = "김약과",
            title = "최근 복용 중인 영양제 조합 상담 부탁드립니다.",
            content = "오메가3, 마그네슘, 유산균을 먹고 있는데 식전/식후 언제가 좋을까요?",
            status = ConsultStatus.COMPLETED,
            isPublic = false,
            images = emptyList(),
            createdAt = yesterday,
            answer = null
        ),
        ConsultItem(
            id = "3",
            userId = "user_1",
            nickName = "김약과",
            title = "두통이 심한데 타이레놀 말고 추천해주세요.",
            content = "타이레놀 먹어도 효과가 별로 없네요.",
            status = ConsultStatus.WAITING,
            isPublic = true,
            images = emptyList(),
            createdAt = yesterday,
            answer = null
        )
    )
}

// 2. 성공 상태 미리보기 (데이터 있음)
@Preview(showBackground = true, name = "Success State (List)")
@Composable
fun MyConsultListScreenSuccessPreview() {
    MyConsultListScreen(
        uiState = MyConsultListUiState(
            isLoading = false,
            myConsults = createMockConsultItems(),
            userMessage = null
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateUp = {},
        onItemClick = {}
    )
}

// 3. 데이터 없음 상태 미리보기
@Preview(showBackground = true, name = "Empty State")
@Composable
fun MyConsultListScreenEmptyPreview() {
    MyConsultListScreen(
        uiState = MyConsultListUiState(
            isLoading = false,
            myConsults = emptyList(),
            userMessage = null
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateUp = {},
        onItemClick = {}
    )
}

// 4. 로딩 상태 미리보기
@Preview(showBackground = true, name = "Loading State")
@Composable
fun MyConsultListScreenLoadingPreview() {
    MyConsultListScreen(
        uiState = MyConsultListUiState(
            isLoading = true,
            userMessage = null
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateUp = {},
        onItemClick = {}
    )
}