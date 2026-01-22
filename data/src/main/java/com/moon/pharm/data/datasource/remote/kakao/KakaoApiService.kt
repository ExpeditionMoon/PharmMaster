package com.moon.pharm.data.datasource.remote.kakao

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiService {
    @GET(KakaoApiConst.PATH_SEARCH_CATEGORY)
    suspend fun searchPharmacies(
        @Header(KakaoApiConst.AUTH_HEADER) apiKey: String,
        @Query(KakaoApiConst.PARAM_CATEGORY_GROUP_CODE) categoryCode: String = KakaoApiConst.VALUE_CATEGORY_CODE_PHARMACY,
        @Query(KakaoApiConst.PARAM_X) longitude: String,
        @Query(KakaoApiConst.PARAM_Y) latitude: String,
        @Query(KakaoApiConst.PARAM_RADIUS) radius: Int = KakaoApiConst.DEFAULT_RADIUS,
        @Query(KakaoApiConst.PARAM_SORT) sort: String = KakaoApiConst.VALUE_SORT_DISTANCE
    ): KakaoSearchResponse

    @GET(KakaoApiConst.PATH_SEARCH_KEYWORD)
    suspend fun searchPharmaciesByKeyword(
        @Header(KakaoApiConst.AUTH_HEADER) apiKey: String,
        @Query(KakaoApiConst.PARAM_CATEGORY_GROUP_CODE) categoryCode: String = KakaoApiConst.VALUE_CATEGORY_CODE_PHARMACY,
        @Query(KakaoApiConst.PARAM_QUERY) query: String
    ): KakaoSearchResponse
}