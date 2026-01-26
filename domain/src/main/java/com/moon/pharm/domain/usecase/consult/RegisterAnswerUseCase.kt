package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.pharmacist.GetPharmacistDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterAnswerUseCase @Inject constructor(
    private val repository: ConsultRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getPharmacistDetailUseCase: GetPharmacistDetailUseCase
) {
    operator fun invoke(consultId: String, content: String, pharmacist: Pharmacist): Flow<DataResourceResult<ConsultItem>> = flow {
        emit(DataResourceResult.Loading)

        val currentUserId = getCurrentUserIdUseCase()
        if (currentUserId == null) {
            emit(DataResourceResult.Failure(Exception("User not logged in")))
            return@flow
        }

        val pharmacistResult = getPharmacistDetailUseCase(currentUserId).first()
        if (pharmacistResult is DataResourceResult.Success) {
            val pharmacist = pharmacistResult.resultData
            val newAnswer = ConsultAnswer(
                answerId = java.util.UUID.randomUUID().toString(),
                pharmacistId = pharmacist.userId,
                pharmacistName = pharmacist.name,
                userInfo = null,
                content = content,
                createdAt = System.currentTimeMillis()
            )

            repository.registerAnswer(consultId, newAnswer).collect { result ->
                emit(result)
            }
        } else {
            emit(DataResourceResult.Failure(Exception("Failed to load pharmacist info")))
        }
    }
}