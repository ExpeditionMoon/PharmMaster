package com.moon.pharm.data.datasource

import com.moon.pharm.data.datasource.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import kotlinx.coroutines.flow.Flow

interface ConsultDataSource {
    suspend fun create(consult: ConsultItemDTO)
    fun getConsultItems(): Flow<List<ConsultItemDTO>>
    fun getConsultDetail(id: String): Flow<ConsultItemDTO?>

    suspend fun updateConsultAnswer(consultId: String, answerDto: ConsultAnswerDTO): ConsultItemDTO
}