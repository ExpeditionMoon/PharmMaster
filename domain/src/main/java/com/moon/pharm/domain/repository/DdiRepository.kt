package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.result.DataResourceResult

interface DdiRepository {
    suspend fun analyzeDrugInteractions(drugs: List<String>): DataResourceResult<DdiResult>
    suspend fun extractDrugNamesFromText(ocrRawText: String): DataResourceResult<List<String>>
}