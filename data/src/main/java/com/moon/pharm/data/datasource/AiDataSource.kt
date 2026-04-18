package com.moon.pharm.data.datasource

interface AiDataSource {
    suspend fun analyzeDdi(drugs: List<String>): String
    suspend fun extractDrugNames(safeOcrText: String): String
}