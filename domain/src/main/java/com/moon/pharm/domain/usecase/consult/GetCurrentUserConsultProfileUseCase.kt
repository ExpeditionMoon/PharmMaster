package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

data class CurrentUserConsultProfile(
    val userId: String,
    val nickname: String,
    val isPharmacist: Boolean
)

class GetCurrentUserConsultProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): DataResourceResult<CurrentUserConsultProfile> {
        val userId = authRepository.getCurrentUserId()
            ?: return DataResourceResult.Failure(IllegalStateException("User not logged in"))

        return when (val userResult = userRepository.getUserOnce(userId)) {
            is DataResourceResult.Success -> {
                val user = userResult.resultData
                DataResourceResult.Success(
                    CurrentUserConsultProfile(
                        userId = userId,
                        nickname = user.nickName,
                        isPharmacist = user.userType == UserType.PHARMACIST
                    )
                )
            }
            is DataResourceResult.Failure -> DataResourceResult.Failure(userResult.exception)
            is DataResourceResult.Loading -> DataResourceResult.Loading
        }
    }
}
