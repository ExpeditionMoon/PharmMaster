package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.pharmacist.GetPharmacistDetailUseCase
import com.moon.pharm.domain.usecase.user.GetNicknameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class ConsultDetailResult(
    val consult: ConsultItem,
    val pharmacist: Pharmacist?,
    val isMyConsultToAnswer: Boolean
)

class GetConsultDetailUseCase @Inject constructor(
    private val consultRepository: ConsultRepository,
    private val getNicknameUseCase: GetNicknameUseCase,
    private val getPharmacistDetail: GetPharmacistDetailUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    operator fun invoke(id: String): Flow<DataResourceResult<ConsultDetailResult>> = flow {
        emit(DataResourceResult.Loading)

        val consultResult = consultRepository.getConsultDetail(id)
            .filter { it !is DataResourceResult.Loading }
            .first()

        if (consultResult is DataResourceResult.Success) {
            val originConsult = consultResult.resultData
            val nickName = getNicknameUseCase.getNickname(originConsult.userId)
            val consult = originConsult.copy(nickName = nickName)

            var pharmacist: Pharmacist? = null
            originConsult.pharmacistId?.let { pId ->
                val pharmacistResult = getPharmacistDetail(pId)
                    .filter { it !is DataResourceResult.Loading }
                    .first()
                if (pharmacistResult is DataResourceResult.Success) {
                    pharmacist = pharmacistResult.resultData
                }
            }

            val currentUserId = getCurrentUserIdUseCase()
            val canAnswer = currentUserId != null &&
                    currentUserId == originConsult.pharmacistId &&
                    originConsult.status == ConsultStatus.WAITING

            emit(DataResourceResult.Success(ConsultDetailResult(consult, pharmacist, canAnswer)))
        } else if (consultResult is DataResourceResult.Failure) {
            emit(DataResourceResult.Failure(consultResult.exception))
        }
    }
}