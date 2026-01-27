package com.moon.pharm.domain.usecase.pharmacist

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPharmacistsByPharmacyUseCase @Inject constructor(
    private val authRepository: PharmacistRepository
) {
    operator fun invoke(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return authRepository.getPharmacistsByPlaceId(placeId)
    }
}