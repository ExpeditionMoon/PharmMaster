package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class MyPageData(
    val user: User,
    val consults: List<ConsultItem>
)

class ObserveMyPageDataUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val consultRepository: ConsultRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<DataResourceResult<MyPageData>> {
        val userId = authRepository.getCurrentUserId()
            ?: return flowOf(DataResourceResult.Failure(IllegalStateException("User not logged in")))

        return userRepository.getUser(userId).flatMapLatest { userResult ->
            if (userResult !is DataResourceResult.Success) {
                return@flatMapLatest flowOf(userResult.toMyPageData())
            }

            val user = userResult.resultData
            val consultFlow = if (user.userType == UserType.PHARMACIST) {
                consultRepository.getMyAnsweredConsultList(userId)
            } else {
                consultRepository.getMyConsult(userId)
            }

            consultFlow.map { consultResult ->
                when (consultResult) {
                    is DataResourceResult.Loading -> DataResourceResult.Loading
                    is DataResourceResult.Failure -> DataResourceResult.Failure(consultResult.exception)
                    is DataResourceResult.Success -> DataResourceResult.Success(
                        MyPageData(user = user, consults = consultResult.resultData)
                    )
                }
            }
        }
    }
}

private fun DataResourceResult<*>.toMyPageData(): DataResourceResult<MyPageData> {
    return when (this) {
        is DataResourceResult.Loading -> DataResourceResult.Loading
        is DataResourceResult.Failure -> DataResourceResult.Failure(exception)
        is DataResourceResult.Success -> DataResourceResult.Failure(IllegalStateException("Unexpected success type"))
    }
}
