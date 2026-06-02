package com.moon.pharm.component_ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.moon.pharm.component_ui.model.PharmacyUiModel

class PharmacyListPreviewProvider : PreviewParameterProvider<List<PharmacyUiModel>> {
    override val values = sequenceOf(
        listOf(
            PharmacyUiModel(
                id = "pharm_001",
                placeId = "place_001",
                name = "Moon Pharmacy",
                address = "Seoul Gangnam-gu Teheran-ro 123",
                tel = "02-1234-5678",
                latitude = 37.498095,
                longitude = 127.027610
            ),
            PharmacyUiModel(
                id = "pharm_002",
                placeId = "place_002",
                name = "Star Pharmacy",
                address = "Seoul Seocho-gu Seocho-daero 456",
                tel = "02-9876-5432",
                latitude = 37.495000,
                longitude = 127.015000
            )
        )
    )
}
