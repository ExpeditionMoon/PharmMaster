package com.moon.pharm.component_ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.moon.pharm.domain.model.pharmacy.Pharmacy

/**
 * Preview
 * 약국 리스트 더미 데이터를 공급하는 Provider 클래스
 */
class PharmacyListPreviewProvider : PreviewParameterProvider<List<Pharmacy>> {
    override val values = sequenceOf(
        listOf(
            Pharmacy(
                id = "pharm_001",
                placeId = "place_001",
                name = "달빛약국",
                address = "서울특별시 강남구 테헤란로 123",
                tel = "02-1234-5678",
                latitude = 37.498095,
                longitude = 127.027610
            ),
            Pharmacy(
                id = "pharm_002",
                placeId = "place_002",
                name = "별빛약국",
                address = "서울특별시 서초구 서초대로 456",
                tel = "02-9876-5432",
                latitude = 37.495000,
                longitude = 127.015000
            )
        )
    )
}