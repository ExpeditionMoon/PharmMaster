package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository

class GetPharmacistUseCase(private val repository: ConsultRepository) {
    suspend fun getById(id: String) = repository.getPharmacistById(id)
    suspend fun getByPharmacy(pharmacyId: String) = repository.getPharmacistsByPharmacy(pharmacyId)
}