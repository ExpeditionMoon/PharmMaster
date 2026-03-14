package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.drug.Drug

interface DrugSearchRepository {
    suspend fun searchDrugByName(itemName: String, pageNo: Int = 1): Result<List<Drug>>
}