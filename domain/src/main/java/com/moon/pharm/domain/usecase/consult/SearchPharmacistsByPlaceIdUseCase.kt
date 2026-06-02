package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPharmacistsByPlaceIdUseCase @Inject constructor(
    private val pharmacistRepository: PharmacistRepository
) {
    operator fun invoke(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return pharmacistRepository.getPharmacistsByPlaceId(placeId)
    }
}
