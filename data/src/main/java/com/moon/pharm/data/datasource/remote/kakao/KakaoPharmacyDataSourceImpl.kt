package com.moon.pharm.data.datasource.remote.kakao

import com.moon.pharm.data.BuildConfig
import com.moon.pharm.data.datasource.PharmacySearchDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KakaoPharmacyDataSourceImpl @Inject constructor(
    private val kakaoApi: KakaoApiService
) : PharmacySearchDataSource {

    private val AUTHORIZATION = "${KakaoApiConst.AUTH_PREFIX} ${BuildConfig.KAKAO_REST_API_KEY}"

    override fun searchExternalPharmacies(query: String): Flow<List<PharmacyDTO>> = flow {
        val response = kakaoApi.searchPharmaciesByKeyword(
            apiKey = AUTHORIZATION,
            query = query
        )
        val dtos = response.documents.map { it.toDto() }
        emit(dtos)
    }

    override fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<List<PharmacyDTO>> = flow {
        val response = kakaoApi.searchPharmacies(
            apiKey = AUTHORIZATION,
            longitude = lng.toString(),
            latitude = lat.toString()
        )
        val dtos = response.documents.map { it.toDto() }
        emit(dtos)
    }
}