package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.openapi.DrugSearchResponse

interface DrugSearchDataSource {
    suspend fun searchDrugByName(itemName: String, pageNo: Int = 1): DrugSearchResponse
}