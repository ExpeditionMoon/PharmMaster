package com.moon.pharm.component_ui.theme

import androidx.compose.ui.graphics.Color

// 사용자가 정의한 핵심 색상
val Primary = Color(0xFF1F3B58)
val Secondary = Color(0xFF5E4B3B)
val Tertiary = Color(0xFFB1CBDC)
val White = Color(0xFFFFFFFF) // 기본 흰색 추가
val Black = Color(0xFF000000) // 기본 검은색 추가
val Background = Color(0xFFF2F4F7)
val SecondFont = Color(0xFF767676)
val OnSurface = Color(0xFF1F3B58)
val Placeholder = Color(0xFFBDBDBD)

// Material3 Color Scheme에 맞춘 매핑 (Light Theme 기준)
/* Primary: 화면에서 가장 많이 쓰이는 주요 색상 (버튼, 활성 탭, 주요 아이콘 등) */
val primaryLight = Primary

/* OnPrimary: Primary 색상 위에 올라가는 텍스트나 아이콘 색상 (보통 흰색) */
val onPrimaryLight = White

/* PrimaryContainer: Primary보다 연한 배경색 (강조 박스, 선택된 아이템 배경 등) */
val primaryContainerLight = Tertiary // Primary와 어울리는 연한 톤으로 Tertiary 활용

/* OnPrimaryContainer: PrimaryContainer 위에 올라가는 텍스트 색상 */
val onPrimaryContainerLight = Color(0xFF001E30) // Primary보다 더 진한 텍스트

/* Secondary: Primary 다음으로 강조하고 싶은 요소 (FAB, 보조 버튼, 선택 컨트롤 등) */
val secondaryLight = Secondary

/* OnSecondary: Secondary 색상 위에 올라가는 텍스트나 아이콘 색상 */
val onSecondaryLight = White

/* SecondaryContainer: Secondary보다 연한 배경색 (보조 강조 박스 등) */
val secondaryContainerLight = Color(0xFFE6DED5) // Secondary의 연한 파생색 (임의 생성)

/* OnSecondaryContainer: SecondaryContainer 위에 올라가는 텍스트 색상 */
val onSecondaryContainerLight = Color(0xFF24190F) // Secondary보다 진한 텍스트

/* Tertiary: Primary, Secondary와 대조를 이루거나 포인트를 줄 때 사용 (차트, 강조 등) */
val tertiaryLight = Tertiary

/* OnTertiary: Tertiary 색상 위에 올라가는 텍스트나 아이콘 색상 */
val onTertiaryLight = Color(0xFF00344D) // Tertiary 위에서의 텍스트

/* TertiaryContainer: Tertiary보다 연한 배경색 */
val tertiaryContainerLight = Color(0xFFCDE6F6) // 더 연한 Tertiary

/* OnTertiaryContainer: TertiaryContainer 위에 올라가는 텍스트 색상 */
val onTertiaryContainerLight = Color(0xFF001E30)

/* Error: 오류 상태를 나타내는 색상 (경고 문구, 삭제 버튼 등) */
val errorLight = Color(0xFFBA1A1A)

/* OnError: Error 색상 위에 올라가는 텍스트 색상 */
val onErrorLight = White

/* ErrorContainer: Error보다 연한 배경색 (오류 메시지 박스 배경 등) */
val errorContainerLight = Color(0xFFFFDAD6)

/* OnErrorContainer: ErrorContainer 위에 올라가는 텍스트 색상 */
val onErrorContainerLight = Color(0xFF410002)

/* Background: 앱의 전체적인 배경색 (Scaffold의 기본 배경) */
val backgroundLight = Background // 배경색

/* OnBackground: Background 위에 올라가는 텍스트나 아이콘 색상 (일반 텍스트) */
val onBackgroundLight = Black

/* Surface: 카드, 시트, 메뉴 등 표면 요소의 배경색 */
val surfaceLight = White

/* OnSurface: Surface 위에 올라가는 텍스트나 아이콘 색상 */
val onSurfaceLight = Black

/* --- 스낵바 및 상태 표시용 추가 색상 --- */
val Success = Color(0xFF2E7D32)       // 아이콘/텍스트용 진한 녹색
val SuccessContainer = Color(0xFFE8F5E9) // 배경용 아주 연한 녹색

val InfoContainer = Color(0xFFE3F2FD) // 배경용 아주 연한 하늘색
val OnInfoContainer = Primary // 텍스트용

val Warning = Color(0xFFF57C00)
val WarningContainer = Color(0xFFFFF3E0)