package com.moon.pharm.domain.usecase.pharmacist

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SavePharmacistUseCase @Inject constructor(
    private val repository: PharmacistRepository
) {
    suspend operator fun invoke(pharmacist: Pharmacist): DataResourceResult<Unit> {
        return repository.savePharmacist(pharmacist)
    }
}