package com.moon.pharm.data.datasource.remote.openapi

import com.moon.pharm.data.datasource.DrugSearchDataSource
import javax.inject.Inject

class OpenApiDrugDataSourceImpl @Inject constructor(
    private val openApiService: OpenApiService
) : DrugSearchDataSource {

    override suspend fun searchDrugByName(itemName: String, pageNo: Int): DrugSearchResponse {
        return openApiService.searchDrugByName(
            itemName = itemName,
            pageNo = pageNo
        )
    }
}