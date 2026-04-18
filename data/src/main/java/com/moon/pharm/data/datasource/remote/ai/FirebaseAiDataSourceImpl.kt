package com.moon.pharm.data.datasource.remote.ai

import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI
import com.moon.pharm.data.datasource.AiDataSource
import javax.inject.Inject

class FirebaseAiDataSourceImpl @Inject constructor() : AiDataSource {

    private val generativeModel = Firebase.vertexAI.generativeModel(
        modelName = DdiAiConst.MODEL_NAME,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    override suspend fun analyzeDdi(drugs: List<String>): String {
        val drugListText = drugs.joinToString(", ")
        val prompt = DdiAiConst.buildAnalyzeDdiPrompt(drugListText)

        val response = generativeModel.generateContent(prompt)
        return response.text ?: throw Exception(ERROR_NO_RESPONSE)
    }

    override suspend fun extractDrugNames(safeOcrText: String): String {
        val prompt = DdiAiConst.buildExtractDrugNamesPrompt(safeOcrText)

        val response = generativeModel.generateContent(prompt)
        return response.text ?: throw Exception(ERROR_NO_RESPONSE)
    }

    companion object {
        private const val ERROR_NO_RESPONSE = "Gemini API 응답 없음"
    }
}