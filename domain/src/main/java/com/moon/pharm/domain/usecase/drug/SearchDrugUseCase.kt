package com.moon.pharm.domain.usecase.drug

import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.domain.repository.DrugSearchRepository

class SearchDrugUseCase(
    private val drugSearchRepository: DrugSearchRepository
) {
    suspend operator fun invoke(query: String): Result<List<Drug>> {
        return drugSearchRepository.searchDrugByName(itemName = query)
    }
}
