package com.moon.pharm.data.datasource

import com.moon.pharm.domain.model.consult.ConsultItem
import kotlinx.coroutines.flow.Flow

interface ConsultDataSource {
    suspend fun create(consult: ConsultItem)
    fun getConsultItems(): Flow<List<ConsultItem>>
    fun getConsultDetail(id: String): Flow<ConsultItem?>
}