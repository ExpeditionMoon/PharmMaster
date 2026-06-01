package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AiDataSource
import com.moon.pharm.data.datasource.remote.ai.DdiAiResponse
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.ddi.DdiException
import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DdiAiRepositoryImpl @Inject constructor(
    private val aiDataSource: AiDataSource,
    private val moshi: Moshi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DdiRepository {

    private fun Throwable.toDdiException(): DdiException = when (this) {
        is DdiException -> this
        else -> DdiException.Network()
    }

    override suspend fun analyzeDrugInteractions(drugs: List<String>): DataResourceResult<DdiResult> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toDdiException() }
        ) {
            val jsonResponse = aiDataSource.analyzeDdi(drugs)
            val adapter = moshi.adapter(DdiAiResponse::class.java)
            val resultDto = adapter.fromJson(jsonResponse)

            resultDto?.toDomain() ?: throw DdiException.AnalysisFailed()
        }

    override suspend fun extractDrugNamesFromText(ocrRawText: String): DataResourceResult<List<String>> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toDdiException() }
        ) {
            val jsonResponse = aiDataSource.extractDrugNames(ocrRawText)
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter = moshi.adapter<List<String>>(listType)
            val resultList = adapter.fromJson(jsonResponse)

            if (resultList.isNullOrEmpty()) {
                throw DdiException.EmptyDrug()
            } else {
                resultList
            }
        }
}
