package com.moon.pharm.component_ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.moon.pharm.component_ui.model.PharmacyUiModel

/**
 * Preview
 * ?쎄뎅 由ъ뒪???붾? ?곗씠?곕? 怨듦툒?섎뒗 Provider ?대옒??
 */
class PharmacyListPreviewProvider : PreviewParameterProvider<List<PharmacyUiModel>> {
    override val values = sequenceOf(
        listOf(
            PharmacyUiModel(
                id = "pharm_001",
                placeId = "place_001",
                name = "?щ튆?쎄뎅",
                address = "?쒖슱?밸퀎??媛뺣궓援??뚰뿤?濡?123",
                latitude = 37.498095,
                longitude = 127.027610
            ),
            PharmacyUiModel(
                id = "pharm_002",
                placeId = "place_002",
                name = "蹂꾨튆?쎄뎅",
                address = "?쒖슱?밸퀎???쒖큹援??쒖큹?濡?456",
                latitude = 37.495000,
                longitude = 127.015000
            )
        )
    )
}
