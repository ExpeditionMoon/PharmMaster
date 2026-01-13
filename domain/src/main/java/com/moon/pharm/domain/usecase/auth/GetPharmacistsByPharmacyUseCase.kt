package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPharmacistsByPharmacyUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(placeId: String): Flow<DataResourceResult<List<Pharmacist>>> {
        return authRepository.getPharmacistsByPlaceId(placeId)
    }
}