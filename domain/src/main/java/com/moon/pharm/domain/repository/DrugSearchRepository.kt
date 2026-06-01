package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.domain.result.DataResourceResult

interface DrugSearchRepository {
    suspend fun searchDrugByName(itemName: String, pageNo: Int = 1): DataResourceResult<List<Drug>>
}
