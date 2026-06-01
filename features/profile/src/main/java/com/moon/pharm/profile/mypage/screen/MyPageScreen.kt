package com.moon.pharm.profile.mypage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.snackbar.CustomSnackbar
import com.moon.pharm.component_ui.component.snackbar.SnackbarType
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.BuildConfig
import com.moon.pharm.profile.R
import com.moon.pharm.profile.mypage.screen.component.EditNicknameDialog
import com.moon.pharm.profile.mypage.screen.component.MyPageFooterSection
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuItemData
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuSection
import com.moon.pharm.profile.mypage.screen.component.MyPageProfileCard
import com.moon.pharm.profile.mypage.mapper.asMyPageString
import com.moon.pharm.profile.mypage.viewmodel.MyPageEffect
import com.moon.pharm.profile.mypage.viewmodel.MyPageUiState
import com.moon.pharm.profile.mypage.viewmodel.MyPageViewModel
import com.moon.pharm.profile.util.myPageConsultMenuTitleRes
import com.moon.pharm.component_ui.R as ComponentUiR

@Composable
fun MyPageRoute(
    onNavigateToMyConsultation: () -> Unit,
    onNavigateToMedicationHistory: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyPageEffect.ShowMessage -> snackbarHostState.showSnackbar(
                    effect.message.asMyPageString(context)
                )
                MyPageEffect.NavigateLogin -> onNavigateToLogin()
            }
        }
    }

    MyPageScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateToMyConsultation = onNavigateToMyConsultation,
        onNavigateToMedicationHistory = onNavigateToMedicationHistory,
        onLogout = viewModel::logout,
        onUpdateNickname = viewModel::updateNickname
    )
}

@Composable
fun MyPageScreen(
    uiState: MyPageUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateToMyConsultation: () -> Unit,
    onNavigateToMedicationHistory: () -> Unit,
    onLogout: () -> Unit,
    onUpdateNickname: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var showEditDialog by remember { mutableStateOf(false) }

    if (showEditDialog && uiState.user != null) {
        EditNicknameDialog(
            currentNickname = uiState.user.nickName,
            onDismiss = { showEditDialog = false },
            onConfirm = { newNickname ->
                onUpdateNickname(newNickname)
                showEditDialog = false
            }
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
        if (uiState.isLoading && uiState.user == null) {
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
                        onEditProfileClick = { showEditDialog = true }
                    )

                    val baseTitle = stringResource(user.userType.myPageConsultMenuTitleRes)

                    val formattedCount = uiState.consultHistoryText?.let {
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
                    Text(stringResource(ComponentUiR.string.error_load_data))
                }
            }
        }
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
                isLoading = false,
                user = User(
                    id = "test_user_id",
                    email = "test@moonpharm.com",
                    nickName = "달토끼",
                    userType = UserType.GENERAL,
                    profileImageUrl = null,
                    createdAt = System.currentTimeMillis(),
                    fcmToken = null
                ),
                myConsults = emptyList()
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigateToMyConsultation = {},
            onNavigateToMedicationHistory = {},
            onLogout = {},
            onUpdateNickname = {}
        )
    }
}
