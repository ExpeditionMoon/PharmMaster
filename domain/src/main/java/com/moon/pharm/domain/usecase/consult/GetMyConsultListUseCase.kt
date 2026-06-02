package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyConsultListUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val consultRepository: ConsultRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<DataResourceResult<MyConsultListResult>> {
        val userId = authRepository.getCurrentUserId()
            ?: return flowOf(DataResourceResult.Failure(IllegalStateException("User not logged in")))

        return userRepository.getUser(userId).map { userResult ->
            if (userResult !is DataResourceResult.Success) return@map userResult.toMyConsultListResult()

            val user = userResult.resultData
            val isPharmacist = user.userType == UserType.PHARMACIST
            val consultResult = if (isPharmacist) {
                consultRepository.getMyAnsweredConsultList(userId)
            } else {
                consultRepository.getMyConsult(userId)
            }

            DataResourceResult.Success(
                MyConsultListResult(
                    userId = userId,
                    nickname = user.nickName,
                    isPharmacist = isPharmacist,
                    consultsFlow = consultResult
                )
            )
        }
    }
}

data class MyConsultListResult(
    val userId: String,
    val nickname: String,
    val isPharmacist: Boolean,
    val consultsFlow: Flow<DataResourceResult<List<ConsultItem>>>
)

private fun DataResourceResult<*>.toMyConsultListResult(): DataResourceResult<MyConsultListResult> {
    return when (this) {
        is DataResourceResult.Loading -> DataResourceResult.Loading
        is DataResourceResult.Failure -> DataResourceResult.Failure(exception)
        is DataResourceResult.Success -> DataResourceResult.Failure(IllegalStateException("Unexpected success type"))
    }
}
