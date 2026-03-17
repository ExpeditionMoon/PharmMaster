package com.moon.pharm.data.datasource.remote.openapi

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApiService {
    @GET("1471000/DrbEasyDrugInfoService/getDrbEasyDrugList")
    suspend fun searchDrugByName(
        @Query("serviceKey") serviceKey: String = OpenApiConst.PUBLIC_DATA_SERVICE_KEY,
        @Query("itemName") itemName: String,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = 20,
        @Query("pageNo") pageNo: Int = 1
    ): DrugSearchResponse
}