package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

class ObserveMyPageConsultsUseCase(
    private val consultRepository: ConsultRepository
) {
    operator fun invoke(
        userId: String,
        userType: UserType
    ): Flow<DataResourceResult<List<ConsultItem>>> {
        return if (userType == UserType.PHARMACIST) {
            consultRepository.getMyAnsweredConsultList(userId)
        } else {
            consultRepository.getMyConsult(userId)
        }
    }
}
