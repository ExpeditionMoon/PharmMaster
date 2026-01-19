package com.moon.pharm.domain.usecase.user

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): DataResourceResult<Unit> {
        val userResult = userRepository.saveUser(user)
        if (userResult is DataResourceResult.Failure) return userResult

        val defaultLifeStyle = UserLifeStyle(userId = user.id)
        return userRepository.saveUserLifeStyle(user.id, defaultLifeStyle)
    }
}