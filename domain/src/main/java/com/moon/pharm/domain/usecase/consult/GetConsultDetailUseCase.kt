package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.pharmacist.GetPharmacistDetailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class ConsultDetailResult(
    val consult: ConsultItem,
    val pharmacist: Pharmacist?
)

class GetConsultDetailUseCase @Inject constructor(
    private val consultRepository: ConsultRepository,
    val getPharmacistDetail: GetPharmacistDetailUseCase
) {
    operator fun invoke(id: String): Flow<DataResourceResult<ConsultDetailResult>> = flow {
        emit(DataResourceResult.Loading)

        val consultResult = consultRepository.getConsultDetail(id).first()

        if (consultResult is DataResourceResult.Success) {
            val consult = consultResult.resultData
            var pharmacist: Pharmacist? = null

            consult.answer?.let { answer ->
                val pharmacistResult = getPharmacistDetail(answer.pharmacistId).first()
                if (pharmacistResult is DataResourceResult.Success) {
                    pharmacist = pharmacistResult.resultData
                }
            }
            emit(DataResourceResult.Success(ConsultDetailResult(consult, pharmacist)))
        } else if (consultResult is DataResourceResult.Failure) {
            emit(DataResourceResult.Failure(consultResult.exception))
        }
    }
}