package com.moon.pharm.domain.usecase.pharmacist

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPharmacistDetailUseCase @Inject constructor(
    private val repository: PharmacistRepository
) {
    operator fun invoke(pharmacistId: String): Flow<DataResourceResult<Pharmacist>> {
        return repository.getPharmacistById(pharmacistId)
    }
}