package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SavePharmacyUseCase @Inject constructor(
    private val repository: PharmacyRepository
) {
    suspend operator fun invoke(pharmacy: Pharmacy): DataResourceResult<Unit> {
        return repository.savePharmacy(pharmacy)
    }
}