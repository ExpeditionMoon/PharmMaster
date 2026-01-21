package com.moon.pharm.data.datasource.remote.kakao

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/local/search/category.json")
    suspend fun searchPharmacies(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryCode: String = "PM9",
        @Query("x") longitude: String,
        @Query("y") latitude: String,
        @Query("radius") radius: Int = 2000,
        @Query("sort") sort: String = "distance"
    ): KakaoSearchResponse

    @GET("v2/local/search/keyword.json")
    suspend fun searchPharmaciesByKeyword(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryCode: String = "PM9",
        @Query("query") query: String
    ): KakaoSearchResponse
}