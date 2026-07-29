package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetMyConsultListUseCase(
    private val authRepository: AuthRepository,
    private val consultRepository: ConsultRepository,
    private val userRepository: UserRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<DataResourceResult<MyConsultListResult>> {
        val userId = authRepository.getCurrentUserId()
            ?: return flowOf(DataResourceResult.Failure(IllegalStateException("User not logged in")))

        return userRepository.getUser(userId).flatMapLatest { userResult ->
            if (userResult !is DataResourceResult.Success) {
                return@flatMapLatest flowOf(userResult.toMyConsultListResult())
            }

            val user = userResult.resultData
            val isPharmacist = user.userType == UserType.PHARMACIST
            val consultsFlow = if (isPharmacist) {
                consultRepository.getMyAnsweredConsultList(userId)
            } else {
                consultRepository.getMyConsult(userId)
            }

            consultsFlow.map { consultResult ->
                consultResult.mapResult { consults ->
                    MyConsultListResult(
                        userId = userId,
                        nickname = user.nickName,
                        isPharmacist = isPharmacist,
                        consults = consults
                    )
                }
            }
        }
    }
}

data class MyConsultListResult(
    val userId: String,
    val nickname: String,
    val isPharmacist: Boolean,
    val consults: List<ConsultItem>
)

private fun DataResourceResult<*>.toMyConsultListResult(): DataResourceResult<MyConsultListResult> {
    return when (this) {
        is DataResourceResult.Loading -> DataResourceResult.Loading
        is DataResourceResult.Failure -> DataResourceResult.Failure(exception)
        is DataResourceResult.Success -> DataResourceResult.Failure(IllegalStateException("Unexpected success type"))
    }
}
