package com.moon.pharm.component_ui.component.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moon.pharm.domain.model.pharmacy.Pharmacy // ✅ 도메인 모델 Import
import kotlin.collections.first
import kotlin.collections.isNotEmpty

@Composable
fun PharmacyMap(
    pharmacies: List<Pharmacy>,          // 지도에 표시할 약국 리스트
    selectedPharmacy: Pharmacy? = null,  // 현재 선택된 약국 (카메라 이동용)
    onPharmacyClick: (Pharmacy) -> Unit, // 마커 클릭 시 동작
    onBackClick: () -> Unit,             // 뒤로가기 버튼 동작
    modifier: Modifier = Modifier
) {
    // 1. 초기 카메라 위치 설정 (서울 시청 기준, 실제 앱에선 사용자 위치로 변경 권장)
    val defaultLocation = LatLng(37.5665, 126.9780)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 14f)
    }

    // 2. 데이터 변경 감지: 선택된 약국이 생기거나 리스트가 로드되면 카메라 이동
    LaunchedEffect(selectedPharmacy, pharmacies) {
        if (selectedPharmacy != null) {
            // 선택된 약국으로 줌인하며 이동
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(selectedPharmacy.latitude, selectedPharmacy.longitude), 16f
                )
            )
        } else if (pharmacies.isNotEmpty()) {
            // 선택된 게 없으면 첫 번째 약국으로 이동
            val first = pharmacies.first()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.latitude, first.longitude), 14f
                )
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 3. 구글 지도 렌더링
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,    // 줌 버튼 숨김 (깔끔하게)
                myLocationButtonEnabled = true, // 내 위치 버튼 활성화
                mapToolbarEnabled = false       // 마커 클릭 시 길찾기/구글맵이동 툴바 숨김
            )
        ) {
            // 4. 약국 리스트를 순회하며 마커 생성
            pharmacies.forEach { pharmacy ->
                Marker(
                    state = MarkerState(position = LatLng(pharmacy.latitude, pharmacy.longitude)),
                    title = pharmacy.name,
                    snippet = pharmacy.address,
                    onClick = {
                        onPharmacyClick(pharmacy) // 클릭 이벤트 전달
                        false // false: 기본 동작(정보창 띄우기+카메라 이동) 수행, true: 무시
                    }
                )
            }
        }

        // 5. 뒤로가기 버튼 (지도 위에 띄움)
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart), // 좌측 상단 배치
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.Black
            )
        }
    }
}