package com.moon.pharm.profile.mypage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moon.pharm.designsystem.common.asString
import com.moon.pharm.designsystem.component.bar.PharmTopBar
import com.moon.pharm.designsystem.component.snackbar.CustomSnackbar
import com.moon.pharm.designsystem.component.snackbar.SnackbarType
import com.moon.pharm.designsystem.model.TopBarAction
import com.moon.pharm.designsystem.model.TopBarData
import com.moon.pharm.designsystem.model.TopBarNavigationType
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.util.ThemePreviews
import com.moon.pharm.profile.BuildConfig
import com.moon.pharm.profile.R
import com.moon.pharm.profile.mypage.model.MyPageUserUiModel
import com.moon.pharm.profile.mypage.screen.component.EditNicknameDialog
import com.moon.pharm.profile.mypage.screen.component.MyPageFooterSection
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuItemData
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuSection
import com.moon.pharm.profile.mypage.screen.component.MyPageProfileCard
import com.moon.pharm.profile.mypage.viewmodel.MyPageConsultState
import com.moon.pharm.profile.mypage.viewmodel.MyPageUiState
import com.moon.pharm.profile.mypage.viewmodel.MyPageViewModel
import com.moon.pharm.designsystem.R as UiR

@Composable
fun MyPageRoute(
    onNavigateToMyConsultation: () -> Unit,
    onNavigateToMedicationHistory: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage = uiState.userMessage
    val messageText = userMessage?.asString()

    LaunchedEffect(uiState.isLogoutSuccess) {
        if (uiState.isLogoutSuccess) {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(userMessage) {
        if (userMessage != null && messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.userMessageShown()
        }
    }

    MyPageScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateToMyConsultation = onNavigateToMyConsultation,
        onNavigateToMedicationHistory = onNavigateToMedicationHistory,
        onLogout = viewModel::logout,
        onUpdateNickname = viewModel::updateNickname,
        onRetryConsults = viewModel::retryConsults
    )
}

@Composable
fun MyPageScreen(
    uiState: MyPageUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateToMyConsultation: () -> Unit,
    onNavigateToMedicationHistory: () -> Unit,
    onLogout: () -> Unit,
    onUpdateNickname: (String) -> Unit,
    onRetryConsults: () -> Unit
) {
    val scrollState = rememberScrollState()
    val showEditDialog: MutableState<Boolean> = remember { mutableStateOf(false) }

    if (showEditDialog.value && uiState.user != null) {
        EditNicknameDialog(
            currentNickname = uiState.user.nickName,
            onDismiss = { showEditDialog.value = false },
            onConfirm = onUpdateNickname
        )
    }

    Scaffold(
        topBar = {
            PharmTopBar(
                data = TopBarData(
                    title = stringResource(R.string.mypage_title),
                    navigationType = TopBarNavigationType.None,
                    actions = listOf(
                        TopBarAction(
                            icon = Icons.Default.Settings,
                            onClick = { /* 설정 화면 이동 */ }
                        )
                    )
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(snackbarData = data, type = SnackbarType.ERROR)
            }
        }
    ) { paddingValues ->
        if (uiState.isProfileLoading && uiState.user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            uiState.user?.let { user ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    MyPageProfileCard(
                        user = user,
                        onEditProfileClick = { showEditDialog.value = true }
                    )

                    val baseTitle = stringResource(
                        if (user.isPharmacist) UiR.string.my_answer_title else UiR.string.my_consult_title
                    )

                    val formattedCount = (uiState.consultState as? MyPageConsultState.Content)?.historyText?.let {
                        stringResource(R.string.mypage_consult_history_format, it)
                    }

                    MyPageMenuSection(
                        title = stringResource(R.string.mypage_menu_core_feature),
                        items = getCoreMenuItems(
                            consultTitle = baseTitle,
                            consultCount = formattedCount,
                            onConsultClick = onNavigateToMyConsultation,
                            onMedicationHistoryClick = onNavigateToMedicationHistory
                        )
                    )

                    MyPageConsultStateContent(
                        consultState = uiState.consultState,
                        onRetry = onRetryConsults
                    )

                    MyPageMenuSection(
                        title = stringResource(R.string.mypage_menu_customer_support),
                        items = getSupportMenuItems()
                    )

                    MyPageFooterSection(
                        onLogout = onLogout,
                        appVersion = BuildConfig.VERSION_NAME
                    )
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(UiR.string.error_load_data))
                }
            }
        }
    }
}

@Composable
private fun MyPageConsultStateContent(
    consultState: MyPageConsultState,
    onRetry: () -> Unit
) {
    when (consultState) {
        MyPageConsultState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        MyPageConsultState.Error -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.mypage_consult_load_error))
                TextButton(onClick = onRetry) {
                    Text(text = stringResource(R.string.mypage_consult_retry))
                }
            }
        }
        is MyPageConsultState.Content -> Unit
    }
}

@Composable
private fun getCoreMenuItems(
    consultTitle: String,
    consultCount: String?,
    onConsultClick: () -> Unit,
    onMedicationHistoryClick: () -> Unit
): List<MyPageMenuItemData> {
    return listOf(
        MyPageMenuItemData(
            icon = Icons.Default.Face,
            title = stringResource(R.string.mypage_menu_medication_record),
            onClick = {}
        ),
        MyPageMenuItemData(
            icon = Icons.Default.Face,
            title = stringResource(R.string.mypage_menu_intake_history),
            onClick = onMedicationHistoryClick
        ),
        MyPageMenuItemData(
            icon = Icons.Outlined.Person,
            title = consultTitle,
            count = consultCount,
            onClick = onConsultClick
        )
    )
}

@Composable
private fun getSupportMenuItems(): List<MyPageMenuItemData> {
    return listOf(
        MyPageMenuItemData(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.mypage_menu_notice),
            onClick = {}
        ),
        MyPageMenuItemData(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.mypage_menu_faq),
            onClick = {}
        ),
        MyPageMenuItemData(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.mypage_menu_inquiry),
            onClick = {}
        )
    )
}

@ThemePreviews
@Composable
private fun MyPageScreenPreview() {
    PharmMasterTheme {
        MyPageScreen(
            uiState = MyPageUiState(
                isProfileLoading = false,
                user = MyPageUserUiModel(
                    id = "test_user_id",
                    nickName = "달토끼",
                    profileImageUrl = null,
                    isPharmacist = false
                )
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigateToMyConsultation = {},
            onNavigateToMedicationHistory = {},
            onLogout = {},
            onUpdateNickname = {},
            onRetryConsults = {}
        )
    }
}
