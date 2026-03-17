package com.moon.pharm.search.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.progress.CircularProgressBar
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.search.mapper.asString
import com.moon.pharm.search.screen.component.DrugListItem
import com.moon.pharm.search.viewmodel.SearchMainViewModel
import com.moon.pharm.search.viewmodel.SearchUiState

@Composable
fun SearchMainScreen(
    navController: NavController,
    viewModel: SearchMainViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    uiState.userMessage?.let { message ->
        val messageString = message.asString()
        LaunchedEffect(message) {
            Toast.makeText(context, messageString, Toast.LENGTH_SHORT).show()
            viewModel.userMessageShown()
        }
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = "의약품 검색",
                    navigationType = TopBarNavigationType.Back,
                    onNavigationClick = { navController.popBackStack() },
                    actions = emptyList()
                )
            )
        }
    ) { innerPadding ->
        SearchMainContent(
            modifier = Modifier.padding(innerPadding),
            searchQuery = searchQuery,
            uiState = uiState,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onItemClick = { drug ->
                Toast.makeText(context, "${drug.itemName} 클릭됨", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun SearchMainContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    uiState: SearchUiState,
    onSearchQueryChange: (String) -> Unit,
    onItemClick: (Drug) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("약 이름을 검색해보세요 (예: 타이레놀)") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressBar(modifier = Modifier.align(Alignment.Center))
                }

                !uiState.isSearchExecuted && uiState.drugs.isEmpty() -> {
                    Text(
                        text = "검색어를 입력해주세요.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.drugs.isEmpty() -> {
                    Text(
                        text = "검색 결과가 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.drugs, key = { it.itemSeq }) { drug ->
                            DrugListItem(
                                drug = drug,
                                onClick = onItemClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SearchMainContentPreview_Success() {
    val dummyDrugs = listOf(
        Drug(
            itemSeq = "199303108",
            itemName = "타이레놀정500밀리그람",
            companyName = "(주)한국얀센",
            efficacy = "감기로 인한 발열 및 통증, 두통, 신경통, 근육통, 월경통, 염좌통",
            interaction = "매일 세 잔 이상 정기적으로 술을 마시는 사람이 이 약을 복용하면 간손상이 유발될 수 있습니다.",
            imageUrl = ""
        ),
        Drug(
            itemSeq = "200609341",
            itemName = "어린이타이레놀현탁액",
            companyName = "(주)한국얀센",
            efficacy = "감기로 인한 발열 및 통증",
            interaction = "다른 해열진통제와 함께 복용하지 마세요.",
            imageUrl = ""
        )
    )

    PharmMasterTheme {
        Box(modifier = Modifier.background(PharmTheme.colors.background)) {
            SearchMainContent(
                searchQuery = "타이레놀",
                uiState = SearchUiState(
                    isLoading = false,
                    drugs = dummyDrugs,
                    isSearchExecuted = true
                ),
                onSearchQueryChange = {},
                onItemClick = {}
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SearchMainContentPreview_Loading() {
    PharmMasterTheme {
        Box(modifier = Modifier.background(PharmTheme.colors.background)) {
            SearchMainContent(
                searchQuery = "타이레놀",
                uiState = SearchUiState(
                    isLoading = true,
                    drugs = emptyList(),
                    isSearchExecuted = true
                ),
                onSearchQueryChange = {},
                onItemClick = {}
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SearchMainContentPreview_Empty() {
    PharmMasterTheme {
        Box(modifier = Modifier.background(PharmTheme.colors.background)) {
            SearchMainContent(
                searchQuery = "없는약이름입니다",
                uiState = SearchUiState(
                    isLoading = false,
                    drugs = emptyList(),
                    isSearchExecuted = true
                ),
                onSearchQueryChange = {},
                onItemClick = {}
            )
        }
    }
}