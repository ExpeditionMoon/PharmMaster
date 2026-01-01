package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface ConsultRepository {
    fun createConsult(consultInfo: ConsultItem): Flow<DataResourceResult<Unit>>
    fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>>
    fun getConsultDetail(id: String): Flow<ConsultItem>
    fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist>
    fun getPharmacistById(id: String): Pharmacist?
}