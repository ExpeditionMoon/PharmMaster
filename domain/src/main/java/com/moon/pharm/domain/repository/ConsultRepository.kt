package com.moon.pharm.domain.repository

import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

interface ConsultRepository {
    fun createConsult(consultInfo: ConsultItem): Flow<DataResourceResult<Unit>>
    fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>>
    fun getConsultDetail(id: String): Flow<DataResourceResult<ConsultItem>>
    fun getMyConsult(userId: String): Flow<DataResourceResult<List<ConsultItem>>>
    fun updateConsult(consultId: String, title: String, content: String, isPublic: Boolean): Flow<DataResourceResult<Unit>>
    fun deleteConsult(consultId: String): Flow<DataResourceResult<Unit>>

    fun getMyAnsweredConsultList(userId: String): Flow<DataResourceResult<List<ConsultItem>>>
    fun registerAnswer(consultId: String, answer: ConsultAnswer): Flow<DataResourceResult<ConsultItem>>
    fun deleteConsultAnswer(consultId: String): Flow<DataResourceResult<Unit>>

    suspend fun uploadImage(uri: String, userId: String): String
    suspend fun sendAnswerNotification(targetUserToken: String, consultId: String): DataResourceResult<Unit>
    suspend fun sendNewConsultNotification(targetToken: String, consultId: String): DataResourceResult<Unit>
    suspend fun updatePharmacistNicknameInAnswers(userId: String, newNickname: String): DataResourceResult<Unit>
}