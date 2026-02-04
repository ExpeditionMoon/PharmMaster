package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, newNickname: String): DataResourceResult<Unit> {
        val updatedUser = user.copy(nickName = newNickname)
        return userRepository.saveUser(updatedUser)
    }
}