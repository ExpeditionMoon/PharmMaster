package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchPharmacyUseCase @Inject constructor(
    private val repository: PharmacyRepository
) {
    operator fun invoke(query: String): Flow<DataResourceResult<List<Pharmacy>>> {
        return repository.searchPharmacies(query).map { result ->
            result.mapResult { list ->
                list.sortedBy { it.name }
            }
        }
    }
}