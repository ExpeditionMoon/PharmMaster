package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.Pharmacist
import kotlinx.coroutines.flow.Flow

interface ConsultRepository {
    fun getConsultItems(): Flow<List<ConsultItem>>
    fun getConsultDetail(id: String): Flow<ConsultItem>
    suspend fun createConsult(
        expertId: String, title: String, content: String, images: List<String>
    ): Result<Unit>
    suspend fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist>
    suspend fun getPharmacistById(id: String): Pharmacist?
}