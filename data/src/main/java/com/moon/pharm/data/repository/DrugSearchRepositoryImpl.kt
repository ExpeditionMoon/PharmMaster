package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.DrugSearchDataSource
import com.moon.pharm.data.datasource.remote.openapi.OpenApiConst
import com.moon.pharm.data.datasource.remote.openapi.toDomain
import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.domain.repository.DrugSearchRepository
import javax.inject.Inject

class DrugSearchRepositoryImpl @Inject constructor(
    private val drugSearchDataSource: DrugSearchDataSource
) : DrugSearchRepository {

    override suspend fun searchDrugByName(itemName: String, pageNo: Int): Result<List<Drug>> {
        return try {
            val response = drugSearchDataSource.searchDrugByName(itemName, pageNo)

            if (response.header?.resultCode == OpenApiConst.RESULT_CODE_SUCCESS) {
                val domainDrugs = response.body?.items?.map { it.toDomain() } ?: emptyList()
                Result.success(domainDrugs)
            } else {
                val errorMsg = response.header?.resultMsg ?: OpenApiConst.DEFAULT_ERROR_MSG
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}