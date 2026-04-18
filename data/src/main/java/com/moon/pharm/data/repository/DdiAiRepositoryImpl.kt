package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AiDataSource
import com.moon.pharm.data.datasource.remote.ai.DdiAiResponse
import com.moon.pharm.domain.model.ddi.DdiException
import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class DdiAiRepositoryImpl @Inject constructor(
    private val aiDataSource: AiDataSource,
    private val moshi: Moshi
) : DdiRepository {

    override suspend fun analyzeDrugInteractions(drugs: List<String>): DataResourceResult<DdiResult> {
        return try {
            val jsonResponse = aiDataSource.analyzeDdi(drugs)
            val adapter = moshi.adapter(DdiAiResponse::class.java)
            val resultDto = adapter.fromJson(jsonResponse)

            if (resultDto != null) {
                DataResourceResult.Success(resultDto.toDomain())
            } else {
                DataResourceResult.Failure(DdiException.AnalysisFailed())
            }
        } catch (e: Exception) {
            DataResourceResult.Failure(DdiException.Network())
        }
    }

    override suspend fun extractDrugNamesFromText(ocrRawText: String): DataResourceResult<List<String>> {
        return try {
            val jsonResponse = aiDataSource.extractDrugNames(ocrRawText)
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter = moshi.adapter<List<String>>(listType)
            val resultList = adapter.fromJson(jsonResponse)

            if (resultList.isNullOrEmpty()) {
                DataResourceResult.Failure(DdiException.EmptyDrug())
            } else {
                DataResourceResult.Success(resultList)
            }
        } catch (e: Exception) {
            DataResourceResult.Failure(DdiException.Network())
        }
    }
}