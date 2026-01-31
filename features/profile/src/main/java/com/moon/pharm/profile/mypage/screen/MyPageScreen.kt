package com.moon.pharm.profile.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.moon.pharm.component_ui.component.bar.PharmTopBar
import com.moon.pharm.component_ui.component.button.PharmOutlinedButton
import com.moon.pharm.component_ui.model.TopBarAction
import com.moon.pharm.component_ui.model.TopBarData
import com.moon.pharm.component_ui.model.TopBarNavigationType
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Tertiary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.mypage.viewmodel.MyPageUiState
import com.moon.pharm.profile.mypage.viewmodel.MyPageViewModel

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
                    title = "마이페이지",
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
                    ProfileCard(user = user)

                    val consultCount = uiState.myConsults.size
                    val consultTitle =
                        if (consultCount > 0) "나의 상담 내역 ($consultCount)" else "나의 상담 내역"

                    MenuSection(
                        title = "핵심 기능",
                        items = listOf(
                            MenuItemData(Icons.Default.Face, "나의 약 기록", {}),
                            MenuItemData(Icons.Default.Notifications, "복용 알림 설정", {}),
                            MenuItemData(
                                Icons.Outlined.Person,
                                consultTitle,
                                onNavigateToMyConsultation
                            )
                        )
                    )
                    MenuSection(
                        title = "고객 지원",
                        items = listOf(
                            MenuItemData(Icons.Outlined.Info, "공지사항", {}),
                            MenuItemData(Icons.Outlined.Info, "자주 묻는 질문", {}),
                            MenuItemData(Icons.Outlined.Info, "1:1 문의", {})
                        )
                    )

                    FooterSection(onLogout = onLogout)
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("정보를 불러올 수 없습니다.")
                }
            }
        }
    }
}

@Composable
fun ProfileCard(user: User) {
    val userTypeLabel = if (user.userType == UserType.PHARMACIST) "약사 회원" else "일반 회원"

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (!user.profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = user.profileImageUrl,
                            contentDescription = "프로필 이미지",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Face, contentDescription = null, tint = White)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "${user.nickName}님",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Text(text = userTypeLabel, fontSize = 14.sp, color = SecondFont)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            PharmOutlinedButton(
                onClick = { /* 내 정보 수정 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("내 정보 수정", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

data class MenuItemData(val icon: ImageVector, val title: String, val onClick: () -> Unit)

@Composable
fun MenuSection(title: String, items: List<MenuItemData>) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            color = SecondFont,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    MenuRowItem(item)
                    if (index < items.lastIndex) {
                        HorizontalDivider(
                            color = backgroundLight,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuRowItem(item: MenuItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(vertical = 18.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Tertiary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Primary
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Tertiary
        )
    }
}

@Composable
fun FooterSection(onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SimpleLinkItem("이용 약관")
                HorizontalDivider(
                    color = backgroundLight,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                SimpleLinkItem("개인정보 처리방침")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "App Version v1.0.0",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = SecondFont,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        PharmOutlinedButton(
            onClick = { /* 내 정보 수정 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그아웃", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "회원 탈퇴",
                color = SecondFont,
                fontSize = 13.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* 회원 탈퇴 로직 */ }
            )
        }
    }
}

@Composable
fun SimpleLinkItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, fontSize = 14.sp, color = Primary)
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Tertiary
        )
    }
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