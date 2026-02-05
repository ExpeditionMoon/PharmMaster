package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val consultRepository: ConsultRepository,
    private val pharmacistRepository: PharmacistRepository
) {
    suspend operator fun invoke(user: User, newNickname: String): DataResourceResult<Unit> = coroutineScope {
        val updatedUser = user.copy(nickName = newNickname)
        val userUpdateResult = userRepository.saveUser(updatedUser)

        if (userUpdateResult is DataResourceResult.Failure) {
            return@coroutineScope userUpdateResult
        }

        if (user.userType == UserType.PHARMACIST) {
            val consultUpdateDeferred = async {
                consultRepository.updatePharmacistNicknameInAnswers(user.id, newNickname)
            }
            val pharmacistUpdateDeferred = async {
                pharmacistRepository.updatePharmacistNickname(user.id, newNickname)
            }
            consultUpdateDeferred.await()
            pharmacistUpdateDeferred.await()
        }
        DataResourceResult.Success(Unit)
    }
}