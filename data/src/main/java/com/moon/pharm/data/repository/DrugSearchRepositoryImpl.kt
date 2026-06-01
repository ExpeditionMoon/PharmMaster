package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.DrugSearchDataSource
import com.moon.pharm.data.datasource.remote.openapi.OpenApiConst
import com.moon.pharm.data.datasource.remote.openapi.toDomain
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.domain.repository.DrugSearchRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DrugSearchRepositoryImpl @Inject constructor(
    private val drugSearchDataSource: DrugSearchDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DrugSearchRepository {

    override suspend fun searchDrugByName(
        itemName: String,
        pageNo: Int
    ): DataResourceResult<List<Drug>> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it }
        ) {
            val response = drugSearchDataSource.searchDrugByName(itemName, pageNo)

            if (response.header?.resultCode == OpenApiConst.RESULT_CODE_SUCCESS) {
                response.body?.items?.map { it.toDomain() } ?: emptyList()
            } else {
                val errorMsg = response.header?.resultMsg ?: OpenApiConst.DEFAULT_ERROR_MSG
                throw Exception(errorMsg)
            }
        }
}
