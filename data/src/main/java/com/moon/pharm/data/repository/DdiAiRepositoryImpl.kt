package com.moon.pharm.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.RequestTimeoutException
import com.moon.pharm.data.datasource.AiDataSource
import com.moon.pharm.data.datasource.remote.ai.DdiAiResponse
import com.moon.pharm.domain.model.ddi.DdiException
import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.model.prescription.PrescriptionException
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
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
        } catch (exception: Exception) {
            DataResourceResult.Failure(exception.toDdiException())
        }
    }

    override suspend fun extractDrugNamesFromText(ocrRawText: String): DataResourceResult<List<String>> {
        return try {
            val jsonResponse = aiDataSource.extractDrugNames(ocrRawText)
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter = moshi.adapter<List<String>>(listType)
            val resultList = adapter.fromJson(jsonResponse.normalizeJsonArray())

            if (resultList.isNullOrEmpty()) {
                DataResourceResult.Failure(PrescriptionException.DrugNameNotFound)
            } else {
                DataResourceResult.Success(resultList)
            }
        } catch (exception: Exception) {
            DataResourceResult.Failure(exception.toPrescriptionException())
        }
    }

    private fun Exception.toDdiException(): DdiException = when (this) {
        is FirebaseNetworkException,
        is IOException,
        is RequestTimeoutException -> DdiException.Network()
        else -> DdiException.AnalysisFailed(message)
    }

    private fun Exception.toPrescriptionException(): PrescriptionException = when (this) {
        is FirebaseNetworkException,
        is IOException,
        is RequestTimeoutException -> PrescriptionException.Network
        is FirebaseAIException -> PrescriptionException.Gemini
        else -> PrescriptionException.Gemini
    }

    private fun String.normalizeJsonArray(): String {
        val trimmedResponse = trim()
        val startIndex = trimmedResponse.indexOf('[')
        val endIndex = trimmedResponse.lastIndexOf(']')

        return if (startIndex in 0..<endIndex) {
            trimmedResponse.substring(startIndex, endIndex + 1)
        } else {
            trimmedResponse
        }
    }
}
