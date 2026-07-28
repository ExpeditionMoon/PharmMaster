package com.moon.pharm.consult.mapper

import com.moon.pharm.consult.model.PharmacyUiModel
import com.moon.pharm.maps.model.MapPlace
import org.junit.Assert.assertEquals
import org.junit.Test

class ConsultPresentationMapperTest {

    @Test
    fun `약국 UI 모델을 지도 전용 위치 모델로 변환한다`() {
        val pharmacy = PharmacyUiModel(
            id = "pharmacy-id",
            placeId = "place-id",
            name = "문약국",
            address = "서울시 강남구",
            tel = "02-1234-5678",
            latitude = 37.5,
            longitude = 127.0
        )

        assertEquals(
            MapPlace(
                id = "pharmacy-id",
                placeId = "place-id",
                name = "문약국",
                address = "서울시 강남구",
                tel = "02-1234-5678",
                latitude = 37.5,
                longitude = 127.0
            ),
            pharmacy.toMapPlace()
        )
    }
}
