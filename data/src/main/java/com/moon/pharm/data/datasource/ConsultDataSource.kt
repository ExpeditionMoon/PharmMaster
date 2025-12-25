package com.moon.pharm.data.datasource

import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.model.ConsultItem
import kotlinx.coroutines.flow.Flow

interface ConsultDataSource {
    suspend fun create(consult: ConsultItem)

    fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>>

    fun getConsultDetail(id: String): Flow<ConsultItem>
}