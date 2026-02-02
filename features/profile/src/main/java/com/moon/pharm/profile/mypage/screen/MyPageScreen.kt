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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.BuildConfig
import com.moon.pharm.profile.R
import com.moon.pharm.profile.mypage.screen.component.MyPageFooterSection
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuItemData
import com.moon.pharm.profile.mypage.screen.component.MyPageMenuSection
import com.moon.pharm.profile.mypage.screen.component.MyPageProfileCard
import com.moon.pharm.profile.mypage.viewmodel.MyPageUiState
import com.moon.pharm.profile.mypage.viewmodel.MyPageViewModel
import com.moon.pharm.profile.util.myPageConsultMenuTitleRes
import com.moon.pharm.component_ui.R as UiR

@Composable
fun MyPageRoute(
    onNavigateToMyConsultation: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MyPageScreen(
        uiState = uiState,
        onNavigateToMyConsultation = onNavigateToMyConsultation,
        onLogout = {
            viewModel.logout()
            onNavigateToLogin()
        }
    )
}

@Composable
fun MyPageScreen(
    uiState: MyPageUiState,
    onNavigateToMyConsultation: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                    MyPageProfileCard(user = user)

                    val baseTitle = stringResource(user.userType.myPageConsultMenuTitleRes)

                    val formattedCount = uiState.consultHistoryText?.let {
                        stringResource(R.string.mypage_consult_history_format, it)
                    }

                    MyPageMenuSection(
                        title = stringResource(R.string.mypage_menu_core_feature),
                        items = getCoreMenuItems(
                            consultTitle = baseTitle,
                            consultCount = formattedCount,
                            onConsultClick = onNavigateToMyConsultation
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
                    Text(stringResource(UiR.string.error_load_data))
                }
            }
        }
    }
}

@Composable
private fun getCoreMenuItems(
    consultTitle: String,
    consultCount: String?,
    onConsultClick: () -> Unit
): List<MyPageMenuItemData> {
    return listOf(
        MyPageMenuItemData(
            icon = Icons.Default.Face,
            title = stringResource(R.string.mypage_menu_medication_record),
            onClick = {}
        ),
        MyPageMenuItemData(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.mypage_menu_alarm_setting),
            onClick = {}
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

@Preview(showBackground = true, name = "마이페이지 - 일반 회원")
@Composable
fun MyPageScreenPreviewSuccess() {
    val mockUser = User(
        id = "test",
        email = "test@test.com",
        nickName = "홍길동",
        userType = UserType.GENERAL,
        profileImageUrl = null,
        createdAt = System.currentTimeMillis(),
        fcmToken = null
    )

    MaterialTheme {
        Surface(color = backgroundLight) {
            MyPageScreen(
                uiState = MyPageUiState(
                    isLoading = false,
                    user = mockUser,
                    myConsults = emptyList()
                ),
                onNavigateToMyConsultation = {},
                onLogout = {}
            )
        }
    }
}